/*
    C ECHO client example using sockets
*/
#include<stdio.h> //printf
#include<string.h>    //strlen
#include<sys/socket.h>    //socket
#include<arpa/inet.h> //inet_addr

#include <errno.h>
#include <wiringSerial.h>


//include for adc soundSensor

#include <wiringPi.h>
#include <wiringPiSPI.h>
#include <unistd.h>

#define CS_MCP3008  6        // BCM_GPIO 25
#define SPI_CHANNEL 0
#define SPI_SPEED   1000000  // 1MHz

int read_mcp3008_adc(unsigned char adcChannel)
{
  unsigned char buff[3];
  int adcValue = 0;

  buff[0] = 0x06 | ((adcChannel & 0x07) >> 2);
  buff[1] = ((adcChannel & 0x07) << 6);
  buff[2] = 0x00;

  digitalWrite(CS_MCP3008, 0);  // Low : CS Active

  wiringPiSPIDataRW(SPI_CHANNEL, buff, 3);

  buff[1] = 0x0F & buff[1];
  adcValue = ( buff[1] << 8) | buff[2];

  digitalWrite(CS_MCP3008, 1);  // High : CS Inactive

  return adcValue;
}


 
int main(int argc , char *argv[])
{


    int sock;
    struct sockaddr_in server;
    char message[1000] , server_reply[2000];
     
    //Create socket
    sock = socket(AF_INET , SOCK_STREAM , 0);
    if (sock == -1)
    {
        printf("Could not create socket");
    }
    puts("Socket created");
     
    server.sin_addr.s_addr = inet_addr("192.168.0.78");
    server.sin_family = AF_INET;
    server.sin_port = htons( 8080 );
 
    //Connect to remote server
    if (connect(sock , (struct sockaddr *)&server , sizeof(server)) < 0)
    {
        perror("connect failed. Error");
        return 1;
    }
     
    puts("Connected\n");
     
	  int adcChannel = 0;
	  int adcValue   = 0;
	
	  if(wiringPiSetup() == -1) return 1;
	  if(wiringPiSPISetup(SPI_CHANNEL, SPI_SPEED) == -1) return 1;
	
	  pinMode(CS_MCP3008, OUTPUT);
		
	  int isSnore = 0;

    //keep communicating with server

	while(1){
	
/*	
		printf("Enter message : ");
	        scanf("%s" , message);
	         
	        //Send some data
	        if( send(sock , message , strlen(message) , 0) < 0)
	        {
	            puts("Send failed");
	            return 1;
	        }
	         
	        //Receive a reply from the server
	        if( recv(sock , server_reply , 2000 , 0) < 0)
	 	{
	            puts("recv failed");
	            break;
	        }
	         
	        puts("Server reply :");
	        puts(server_reply);
*/

       	    adcValue = read_mcp3008_adc(adcChannel);
		    printf("adc0 Value = %d\n", adcValue);
		
		    if(adcValue > 100){
		
			isSnore = 1;
			//break;
		    }
		    else {
		
			isSnore = 0;
			//break;
		    }
		
		    usleep(500000);	//0.2s
		 	
	
			//Send some data
			char char_arr[1];
			char_arr[0] = isSnore + '0';

			//printf("at for loop : %d\n" , isSnore);
		        if( send(sock , char_arr , strlen(char_arr) , 0) < 0)
		        {
		            puts("Send failed");
		            return 1;
		        }
		         
			//printf("after send socket\n");
			//printf("print %d \n" , char_arr[0]);



		        //Receive a reply from the server
		        if( recv(sock , server_reply , strlen(char_arr)+4 , 0) < 0)
		 	{
		            puts("recv failed");
		            break;
		        }
		        //printf("after recieve socket\n");
			printf("%s\n" , server_reply);			
 
		        puts(" Server reply :");
			//puts(" ");
       			puts(server_reply);

	}
         
     
    close(sock);
    return 0;
}
