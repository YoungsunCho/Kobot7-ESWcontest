
#include<Wire.h>

const int MPU_addr=0x68;  // I2C address of the MPU-6050
int16_t AcX,AcY,AcZ,Tmp,GyX,GyY,GyZ;


int accel_reading;
int accel_corrected;
int accel_offset = 200;   //offset 설정의 의미??
float accel_angle;
float accel_scale = 1;
 
int gyro_offset = 151;    //offset 설정의 의미??
double dgy_x,deg;
double angle;
float last_read_time;
float last_x_angle,last_y_angle,last_z_angle;

   unsigned long totalTime = 0;
   unsigned long startTime = 0;
   unsigned long endTime = 0;

   bool isPressure = false;
   
   //각각의 자세마다 시간 측정
   unsigned long startLeftTime = 0; 
   unsigned long startCenterTime = 0;
   unsigned long startRightTime = 0;

   unsigned long endLeftTime = 0; 
   unsigned long endCenterTime = 0;
   unsigned long endRightTime = 0;

   unsigned long totalLeftTime = 0; 
   unsigned long totalCenterTime = 0;
   unsigned long totalRightTime = 0;

   String currentPosition = "";

void setup(){
  Wire.begin();
  Wire.beginTransmission(MPU_addr);
  Wire.write(0x6B);  // PWR_MGMT_1 register
  Wire.write(0);     // set to zero (wakes up the MPU-6050)
  Wire.endTransmission(true);  
  Serial.begin(9600);
}
void loop(){

  //A0에서 압력센서의 값을 읽어온다.
  int sensorval = analogRead(A0);
  
  Wire.beginTransmission(MPU_addr);
  Wire.write(0x3B);  // starting with register 0x3B (ACCEL_XOUT_H)
  Wire.endTransmission(false);
  Wire.requestFrom(MPU_addr,14,true);  // request a total of 14 registers
  AcX=Wire.read()<<8|Wire.read();  // 0x3B (ACCEL_XOUT_H) & 0x3C (ACCEL_XOUT_L)     
  AcY=Wire.read()<<8|Wire.read();  // 0x3D (ACCEL_YOUT_H) & 0x3E (ACCEL_YOUT_L)
  AcZ=Wire.read()<<8|Wire.read();  // 0x3F (ACCEL_ZOUT_H) & 0x40 (ACCEL_ZOUT_L)
  Tmp=Wire.read()<<8|Wire.read();  // 0x41 (TEMP_OUT_H) & 0x42 (TEMP_OUT_L)
  GyX=Wire.read()<<8|Wire.read();  // 0x43 (GYRO_XOUT_H) & 0x44 (GYRO_XOUT_L)
  GyY=Wire.read()<<8|Wire.read();  // 0x45 (GYRO_YOUT_H) & 0x46 (GYRO_YOUT_L)
  GyZ=Wire.read()<<8|Wire.read();  // 0x47 (GYRO_ZOUT_H) & 0x48 (GYRO_ZOUT_L)
  //Serial.print("AcX = "); Serial.print(AcX);
  //Serial.print(" | AcY = "); Serial.print(AcY);
  //Serial.print(" | AcZ = "); Serial.print(AcZ);
  //Serial.print(" | Tmp = "); Serial.print(Tmp/340.00+36.53);  //equation for temperature in degrees C from datasheet
  //Serial.print(" | GyX = "); Serial.print(GyX);
  //Serial.print(" | GyY = "); Serial.print(GyY);
  //Serial.print(" | GyZ = "); Serial.println(GyZ);

  float ax, ay, az;   //scaled accelerometer values
  float gx, gy, gz;

  ax = AcX;
  ay = AcY;
  az = AcZ;

  gx = GyX;
  gy = GyY;
  gz = GyZ;

       accel_reading = ay;
       accel_corrected = accel_reading - accel_offset;
       accel_corrected = map(accel_corrected, -16800, 16800, -90, 90);
       accel_corrected = constrain(accel_corrected, -90, 90);
       accel_angle = (float)(accel_corrected * accel_scale);

//       // 가속도만을 이용한 각도 출력
//       Serial.print("Acc angle : ");
//       Serial.print(accel_angle);
//       Serial.print("\t");
 
       deg = atan2(ax, az) * 180 / PI;     // rad to deg

       // 자이로+가속도 조합한 각도
       dgy_x = gy / gyro_offset;
       angle = (0.95 * (angle + (dgy_x * 0.001))) + (0.05 * deg);
 
       // 자이로 가속도 둘 다 이용한 각도 출력
       //Serial.print("Filter angle : ");
       //Serial.println(angle) ;

       //압력센서값 출력
       //Serial.println("");
       //Serial.print("Pressure sensor : ");
       //Serial.println(sensorval);


  
   //Serial.println("time : ");    
   //Serial.println(millis()/100);


  /*********************************
   * test code
  if(sensorval > 0 && !isPressure ){   
    startTime = millis()/100;
    isPressure = true;
  }//end if
  
  else if(sensorval == 0 && isPressure){
    endTime = millis()/100;
    isPressure = false;

    totalTime += (endTime - startTime)/10;
    Serial.print("total time is : ");
    Serial.println(totalTime);
  }
  **************************************/

  
  if(sensorval > 0 && !isPressure ){   
    startTime = millis()/100;
    isPressure = true;
    
      if(angle < -40 ){
        currentPosition = "left";  
        startLeftTime = millis()/100;
      }
      else if(angle > 40){
        currentPosition = "right";
        startRightTime = millis()/100;
      }
      else {     // -40 < angle < 40
        currentPosition = "center";
        startCenterTime = millis()/100;
      }
     
     Serial.print("Filter ANGLE : ");
     Serial.println(angle) ;
     Serial.println(currentPosition);

     
  }//end if
   else if (sensorval == 0 && isPressure){
    endTime = millis()/100;
    isPressure = false;

     if( currentPosition == "left"){

        totalLeftTime += (endTime - startLeftTime) /10;
        Serial.print("Total Left Time : ");
        Serial.println(totalLeftTime) ;
      }
      else if(currentPosition == "right"){
   
        totalRightTime += (endTime - startRightTime) /10;
        Serial.print("Total Right Time : ");
        Serial.println(totalRightTime) ;
      }
      else {     // center
       
        totalCenterTime += (endTime - startCenterTime) /10;
        Serial.print("Total Center Time : ");
        Serial.println(totalCenterTime) ;
      }
    
    totalTime += (endTime - startTime) / 10;
    Serial.print("Total Sleep Time : ");
    Serial.println(totalTime);


     Serial.print("                           Filter ANGLE : ");
   Serial.println(angle) ;
   Serial.println("*******************************Total*************************");
   Serial.print(" Last Total Sleep Time : ");
   Serial.println(totalTime);
   Serial.print(" Last Total left Time : ");
   Serial.println(totalLeftTime);
   Serial.print(" Last Total Center Time : ");
   Serial.println(totalCenterTime);
   Serial.print(" Last Total Right Time : ");
   Serial.println(totalRightTime);
   Serial.println("***************************************************************");



    
  }
  else if(sensorval != 0 && isPressure){    //if(sensorval != 0 && isPressure)
    
     if( currentPosition == "left"){
        if(angle > 40){   //right
            endLeftTime = millis()/100; 
            
            totalLeftTime += (endLeftTime - startLeftTime) /10;
            Serial.print("Total Left Time : ");
            Serial.println(totalLeftTime) ; 
            
            startRightTime = millis()/100;
            //startRightTime = endLeftTime;
            currentPosition = "right";  
        }  
        else if (angle > -40 && angle < 40){  //center
            endLeftTime = millis()/100;  
            
            totalLeftTime += (endLeftTime - startLeftTime) /10;
            Serial.print("Total Left Time : ");
            Serial.println(totalLeftTime) ;
            
            startCenterTime = millis()/100;
            currentPosition = "center";  
        }
      }
      
      else if(currentPosition == "right"){
        if(angle < -40){   //left
            endRightTime = millis()/100;  

            totalRightTime += (endRightTime - startRightTime) /10;
            Serial.print("Total Right Time : ");
            Serial.println(totalRightTime) ;
        
            startLeftTime = millis()/100;
            currentPosition = "left";  
        }  
        else if (angle > -40 && angle < 40){  //center
            endRightTime = millis()/100;  

            totalRightTime += (endRightTime - startRightTime) /10;
            Serial.print("Total Right Time : ");
            Serial.println(totalRightTime) ;
                
            startCenterTime = millis()/100;
            currentPosition = "center";  
        }


      }
      
      else {     // center
        if(angle > 40){   //right
            endCenterTime = millis()/100;  

            totalCenterTime += (endCenterTime - startCenterTime) /10;
            Serial.print("Total Center Time : ");
            Serial.println(totalCenterTime) ;

            startRightTime = millis()/100;
            currentPosition = "right";  
        }  
        else if (angle < -40 ){  //left
            endCenterTime = millis()/100;  

            totalCenterTime += (endCenterTime - startCenterTime) /10;
            Serial.print("Total Center Time : ");
            Serial.println(totalCenterTime) ;
                
            startLeftTime = millis()/100;
            currentPosition = "left";  
        }

      }

    
  }//end else   //isPressure

 
  //delay(100);

   
}


