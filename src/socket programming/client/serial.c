#include <stdio.h>
#include <string.h>
#include <errno.h>

#include <wiringSerial.h>

int main(){
	int fd;
	if((fd=serialOpen("/dev/ttyUSB2" , 9600)) < 0){
		fprintf(stderr,"Unable to open serial device : %s\n", strerror(errno));
		return 1;

	}

	for(;;){
		//putchar(serialGetchar(fd));

		char posture = serialGetchar(fd);
		//serialGetchar(fd);
		printf("%c\n" , posture);
		//putchar(posture);

		fflush(stdout);
	}



}