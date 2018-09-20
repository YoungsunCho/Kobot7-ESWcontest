/*
    C ECHO client example using sockets
*/
#include<stdio.h> //printf
#include<string.h>    //strlen
#include<sys/socket.h>    //socket
#include<arpa/inet.h> //inet_addr

#include <errno.h>
#include <wiringSerial.h>

 
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
    server.sin_port = htons( 8008 );
 
    //Connect to remote server
    if (connect(sock , (struct sockaddr *)&server , sizeof(server)) < 0)
    {
        perror("connect failed. Error");
        return 1;
    }
     
    puts("Connected\n");
     
    //keep communicating with server
    while(1)
    {

	//-------------------------------------------------
/*	
        printf("Enter message : ");
        scanf("%s" , message);
        printf("%d", strlen(message));
         
        //Send some data
        if( send(sock , message , strlen(message) , 0) < 0)
        {
            puts("Send failed");
            return 1;
        }
         
        //Receive a reply from the server
        if( recv(sock , server_reply , strlen(message)+4, 0) < 0)
 	{
            puts("recv failed");
            break;
        }
         
        puts("Server reply :");
        puts(server_reply);
*/	
	//-----------------------------------------------------

	int fd;
	if((fd=serialOpen("/dev/rfcomm0" , 9600)) < 0){
		fprintf(stderr,"Unable to open serial device : %s\n", strerror(errno));
		return 1;

	}

	for(;;){
		//putchar(serialGetchar(fd));
		int posture = serialGetchar(fd);

			//Send some data
			char char_arr[1];
			char_arr[0] = posture -2 +48 ;

			printf("%d\n" , posture-2);
		        if( send(sock , char_arr , strlen(char_arr) , 0) < 0)
		        {
		            puts("Send failed");
		            return 1;
		        }
		         
		        //Receive a reply from the server
		        if( recv(sock , server_reply , strlen(char_arr)+4 , 0) < 0)
		 	{
		            puts("recv failed");
		            break;
		        }
		        
			//printf("%s\n" , server_reply);			
 
		        //puts(" Server reply :");
			puts(" ");
       			//puts(server_reply);

		fflush(stdout);
	}
         
        

	
    }
     
    close(sock);
    return 0;
}
