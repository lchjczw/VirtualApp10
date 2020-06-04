#ifndef __SUBSTRATEHOOK_H__
#define __SUBSTRATEHOOK_H__


#include <stdlib.h>

#define _extern extern "C" __attribute__((__visibility__("default")))

#ifdef __cplusplus
extern "C" {
#endif


//kook add
//typedef struct __SubstrateProcess *SubstrateProcessRef;
//static void SubstrateHookFunction(SubstrateProcessRef process, void *symbol, void *replace, void **result);
// kook add end

void MSHookFunction(void *symbol, void *replace, void **result);

#ifdef __cplusplus
}
#endif

#endif
