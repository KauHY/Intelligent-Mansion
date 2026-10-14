/*
* Copyright (c) 2020 AIIT XUOS Lab
* XiUOS is licensed under Mulan PSL v2.
* You can use this software according to the terms and conditions of the Mulan PSL v2.
* You may obtain a copy of Mulan PSL v2 at:
*        http://license.coscl.org.cn/MulanPSL2
* THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
* EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
* MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
* See the Mulan PSL v2 for more details.
*/

#include <stdio.h>
#include <string.h>
// #include <user_api.h>
#include <transform.h>



void* task1_func(void* param) {
    while(1) {
	    printf("task1 is running\n");
	    UserTaskDelay(5000);
    }
}

void* task2_func(void* param) {
    while(1) {
	    printf("task2 is running\n");
	    UserTaskDelay(5000);
    }
}

int task_management(void) {
    int ret = 0; 
    UtaskType task1; 
    task1.prio = 25; 
    task1.stack_size = 2048; 
    task1.func_entry = task1_func; 
    task1.func_param = NULL; 
    strncpy(task1.name,"task1",strlen("task1")); 

    int task1_pid = UserTaskCreate(task1); 
    if (task1_pid < 0) 
	    return -1; 
    UserTaskCoreCombine(task1_pid,0); 
    UserTaskCoreUnCombine(task1_pid); 
    ret = UserTaskStartup(task1_pid); 

    UtaskType task2;
    task2.prio = 24;
    task2.stack_size = 2048;
    task2.func_entry = task2_func;
    task2.func_param = NULL;
    strncpy(task2.name,"task2",strlen("task2"));

    int task2_pid = UserTaskCreate(task2);
    if (task2_pid < 0)
	    return -1;

    UserTaskCoreCombine(task2_pid,1);
    ret = UserTaskStartup(task2_pid);
    //UserTaskDelete(task2_pid);
}

extern int FrameworkInit();
extern void ApplicationOtaTaskInit(void);
int main(void)
{
	printf("Hello, Prof. Wan, Prof. Yan, and Prof Li! \n");
	FrameworkInit();
#ifdef APPLICATION_OTA
	ApplicationOtaTaskInit();
#endif
	task_management();
    return 0;
}
// int cppmain(void);


