/*******************************************************************************
 *
 * usrAppInit.c 
 *
 * Copyright(c) 2012-2017 Hancom MDS Co.,Ltd.
 * All rights reserved.
 *
 * Brief history
 * -------------
 *
 ******************************************************************************/
 
#include <neos.h>
#include <stdio.h>


//for socket communication
#include<string.h>    //strlen
#include<sys/socket.h>
#include<arpa/inet.h> //inet_addr
#include<unistd.h>    //write
#include<netinet/in.h>


extern void hal_main();

void
UserAppInit(void)
{
	UserRAMdiskInit();

	//hal_main();

	//------------------------------------------------------------------

			int socket_desc , client_sock , c , read_size;
		    struct sockaddr_in server , client;

		    //unconditionally make the size of message just 1
		    char client_message[1];


		    //Create socket
		    socket_desc = socket(AF_INET , SOCK_STREAM , 0);
		    if (socket_desc == -1)
		    {
		        printf("Could not create socket");
		    }
		    puts("Socket created");

		    //Prepare the sockaddr_in structure
		    server.sin_family = AF_INET;
		    server.sin_addr.s_addr = INADDR_ANY;
		    server.sin_port = htons( 8888 );

		    //Bind
		    if( bind(socket_desc,(struct sockaddr *)&server , sizeof(server)) < 0)
		    {
		        //print the error message
		        perror("bind failed. Error");
		        //return 1;
		    }
		    puts("bind done");

		    //Listen
		    listen(socket_desc , 3);

		    //Accept and incoming connection
		    puts("Waiting for incoming connections...");
		    c = sizeof(struct sockaddr_in);

		    //accept connection from an incoming client
		    client_sock = accept(socket_desc, (struct sockaddr *)&client, (socklen_t*)&c);
		    if (client_sock < 0)
		    {
		        perror("accept failed");
		        //return 1;
		    }
		    puts("Connection accepted");

		    //Receive a message from client
		    while( (read_size = recv(client_sock , client_message , strlen(client_message) , 0)) > 0 )
		    {
		        //Send the message back to client
			
			int posture = client_message[0] -48;

		    	printf("Message from client : %d\n", posture);


		        write(client_sock , client_message , strlen(client_message));
		    	//printf("Message from client : %s\n", client_message);
		    	//puts("Message from client : %s\n", client_message);
		    	//puts(client_message);
		    }

		    if(read_size == 0)
		    {
		        puts("Client disconnected");
		        fflush(stdout);
		    }
		    else if(read_size == -1)
		    {
		        perror("recv failed");
		    }

		//------------------------------------------------------------------

}
