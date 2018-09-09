#include <SoftwareSerial.h> //시리얼통신 라이브러리 호출
 
int blueTx=5;   //Tx (보내는핀 설정)
int blueRx=6;   //Rx (받는핀 설정)
SoftwareSerial mySerial(blueTx, blueRx);  //시리얼 통신을 위한 객체선언

#include<Wire.h>

const int MPU_addr=0x68;  // I2C address of the MPU-6050
int16_t AcX,AcY,AcZ,Tmp,GyX,GyY,GyZ;


int accel_reading;
int accel_corrected;
int accel_offset = 200;   //offset 설정

float accel_angle;
float accel_scale = 1;
 
int gyro_offset = 151;    //offset 설정
double dgy_x,deg;
double angle;
float last_read_time;
float last_x_angle,last_y_angle,last_z_angle;

void setup() 
{
  Wire.begin();
  Wire.beginTransmission(MPU_addr);
  Wire.write(0x6B);  // PWR_MGMT_1 register
  Wire.write(0);     // set to zero (wakes up the MPU-6050)
  Wire.endTransmission(true);  

  Serial.begin(9600);   //시리얼모니터
  mySerial.begin(9600); //블루투스 시리얼  

}
void loop()
{
   
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
  
  
  
       deg = atan2(ax, az) * 180 / PI;     // rad to deg

       // 자이로+가속도 조합한 각도
       dgy_x = gy / gyro_offset;
       angle = (0.95 * (angle + (dgy_x * 0.001))) + (0.05 * deg);
       
      if(angle < -40 ){
        //left
        mySerial.write(Serial.println("22"));
        
      }
      else if(angle > 40){
        //right
        mySerial.write(Serial.println("333"));     
        
      }
      else {     // -40 < angle < 40
        //center
        mySerial.write(Serial.println("1"));
    
      }
      
      //delay 1s
      delay(1000);
      
}

