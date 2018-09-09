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
#include <config/bspConfig.h>
#include <pthread.h>
#include <sys/baseTypes.h>
#include <cpu/bcm2835.h>
#include "MeasureSleepState.h"
#include "MeasureSnoring.h"

#define GPIO_GPFSEL_IN 0x00
#define GPIO_GPFSEL_OUT 0x01
#define GPIO_PIN_NUMBER_06 0x06
//#define GPIO_PIN_NUMBER_21 0x15
#define GPIO_PIN_NUMBER_23 0x17
#define GPIO_PIN_NUMBER_24 0x18

#define GPIO_PIN_NUMBER_21 0x15
#define GPIO_PIN_NUMBER_20 0x14
#define GPIO_PIN_NUMBER_16 0x10
#define GPIO_PIN_NUMBER_12 0x0c
#define GPIO_PIN_NUMBER_26 0x1a

#define GPIO_PIN_NUMBER_22 0x16

#define ON 1
#define OFF 0

pthread_t UpdThread1;
pthread_t UpdThread2;
pthread_t UpdThread3;
pthread_mutex_t Locker1;
pthread_mutex_t Locker2;
pthread_mutex_t Locker3;

int sleepState=0;
int smartphoneScreen=0;
int snoring=0;
int vibration=0;
int preSound=0;
int sleepPosture=0;
int value1, value2, value3, value4, value5;

/*In MeasureSnoring*/
int countSnoring = 0;
time_t tStart;
time_t tNow;
int tDiff;

time_t start;
time_t now;
int diff;

int countZero = 0;

int testSnoring()
{
	now = time(NULL);
	diff = difftime(now, start);
	if(diff>=3)
	{
		start = time(NULL);
		return 1;
	}
	else
	{
		return 0;
	}
}

void *UpdateValue1(void* arg)
{
//	while(1)
//   {
//		value1 = BspGpioGetValue(GPIO_PIN_NUMBER_21);
//		printf("pressure value1 : %d \n", value1);
//		value2 = BspGpioGetValue(GPIO_PIN_NUMBER_20);
//		printf("pressure value2 : %d \n", value2);
//		value3 = BspGpioGetValue(GPIO_PIN_NUMBER_26);
//		printf("pressure value3 : %d \n", value3);
//		value4= BspGpioGetValue(GPIO_PIN_NUMBER_16);
//		printf("pressure value4 : %d \n", value4);
//		value5= BspGpioGetValue(GPIO_PIN_NUMBER_12);
//		printf("pressure value5 : %d \n", value5);
//
//		ThreadDelay(300000000);
//   }
}

void *UpdateValue2(void* arg)
{
	int pressure = 0, illuminance = 1;
	while(1)
	{
		pressure = BspGpioGetValue(GPIO_PIN_NUMBER_23);
		illuminance = BspGpioGetValue(GPIO_PIN_NUMBER_06);
		MeasureSleepState(pressure, illuminance, smartphoneScreen);
		//printf("Sleep State : %d \n", sleepState);
		ThreadDelay(200000000);

		int sound = 0;
		//sound = BspGpioGetValue(GPIO_PIN_NUMBER_24);

		sound = testSnoring();
		printf("sound : %d \n", sound);
		MeasureSnoring(sound);

		if(vibration == ON)
		{
			++snoring;
			printf("**********Snore ON***********\n");
			printf("**********Snore ON***********\n");
			printf("**********Snore ON***********\n");
			BspGpioSetValue(GPIO_PIN_NUMBER_22, 1);//for a few seconds
			vibration = OFF;
		}
	}
}

void *UpdateValue3(void* arg)
{

}


void
UserAppInit(void)
{
	UserRAMdiskInit();
	BspGpioSetAlt(GPIO_PIN_NUMBER_06, GPIO_GPFSEL_IN);//illuminance sensor
	//BspGpioSetAlt(GPIO_PIN_NUMBER_21, GPIO_GPFSEL_IN);//pressure sensor(FSR)
	BspGpioSetAlt(GPIO_PIN_NUMBER_24, GPIO_GPFSEL_IN);//sound sensor
	BspGpioSetAlt(GPIO_PIN_NUMBER_22, GPIO_GPFSEL_OUT);//vibration motor
	BspGpioSetAlt(GPIO_PIN_NUMBER_23, GPIO_GPFSEL_IN);//for testing

	BspGpioSetAlt(GPIO_PIN_NUMBER_21, GPIO_GPFSEL_IN);
	BspGpioSetAlt(GPIO_PIN_NUMBER_20, GPIO_GPFSEL_IN);
	BspGpioSetAlt(GPIO_PIN_NUMBER_16, GPIO_GPFSEL_IN);
	BspGpioSetAlt(GPIO_PIN_NUMBER_12, GPIO_GPFSEL_IN);
	BspGpioSetAlt(GPIO_PIN_NUMBER_26, GPIO_GPFSEL_IN);

	start = time(NULL);

	pthread_mutex_init(&Locker1,NULL);
	pthread_mutex_init(&Locker2,NULL);
	pthread_mutex_init(&Locker3,NULL);
	pthread_create(&UpdThread1, NULL, UpdateValue1, NULL);
	pthread_create(&UpdThread2, NULL, UpdateValue2, NULL);
	pthread_create(&UpdThread3, NULL, UpdateValue3, NULL);

	hal_main();

}