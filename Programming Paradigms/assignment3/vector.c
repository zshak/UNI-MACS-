#include "vector.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

void VectorNew(vector *v, int elemSize, VectorFreeFunction freeFn, int initialAllocation)
{
    assert(elemSize > 0);
    v->elemSize = elemSize;
    v->freeFn = freeFn;
    assert(initialAllocation >= 0);
    if(initialAllocation != 0){
        v->capacity = initialAllocation;
    }else{
        v->capacity = 4;
    }
    v->logLen = 0;
    v->elems = malloc(v->capacity * elemSize);
    assert(v->elems);
}

void VectorDispose(vector *v)
{
    if(v->freeFn){
        for(int i = 0; i < v->logLen; i++){
            v->freeFn((char*)v->elems + i*v->elemSize);
        }
    }
    free(v->elems);
}

int VectorLength(const vector *v)
 { 
    return v->logLen;
 }

void *VectorNth(const vector *v, int position)
 { 
    assert(position >= 0 && position < v->logLen);
    return (void*)((char*)v->elems + position*v->elemSize);
 }

void VectorReplace(vector *v, const void *elemAddr, int position)
{
    assert(position >= 0 && position < v->logLen);
    void* elemToReplace = (char*)v->elems + position*v->elemSize;
    if(v->freeFn){
        v->freeFn(elemToReplace);
    }

    memcpy(elemToReplace, elemAddr, v->elemSize);
}

void expand(vector* v){
    v->capacity*=2;
    v->elems = realloc(v->elems, v->capacity * v->elemSize);
    assert(v->elems);
}

void VectorInsert(vector *v, const void *elemAddr, int position)
{
    assert(position >= 0 && position <= v->logLen);
    if(v->logLen == v->capacity) expand(v);
    for(int i = v->logLen; i > position; i--){
        void* dest = (char*)v->elems + i*v->elemSize;
        void* from = (char*)dest - v->elemSize;
        memcpy(dest, from, v->elemSize);
    }
    memcpy((char*)v->elems + v->elemSize* position, elemAddr, v->elemSize);
    v->logLen++;
}

void VectorAppend(vector *v, const void *elemAddr)
{
    if(v->logLen == v->capacity) expand(v);
    memcpy((char*)v->elems + v->elemSize*v->logLen,
        elemAddr,v->elemSize);
    v->logLen++;
}

void VectorDelete(vector *v, int position)
{
    assert(position >= 0 && position < v->logLen);
    if(v->freeFn) v->freeFn((char*)v->elems + position*v->elemSize);
    for(int i = position; i < v-> logLen - 1; i++){
        void* dest = (char*)v->elems + i* v->elemSize;
        void* from = (char*)dest + v->elemSize;
        memcpy(dest, from, v->elemSize);
    }
    v->logLen--;
}

void VectorSort(vector *v, VectorCompareFunction compare)
{
    assert(compare);
    qsort(v->elems,v->logLen, v->elemSize, compare);
}

void VectorMap(vector *v, VectorMapFunction mapFn, void *auxData)
{
    assert(mapFn);
    for(int i = 0; i < v->logLen; i++){
        mapFn((void*)((char*)v->elems + i*v->elemSize),auxData);
    }
}

static const int kNotFound = -1;
int VectorSearch(const vector *v, const void *key, VectorCompareFunction searchFn, int startIndex, bool isSorted)
{ 
    assert(startIndex >= 0 && startIndex <= v->logLen && key && searchFn);

    void* result = NULL;

    if(isSorted){
        result = bsearch(key, (char*)v->elems + startIndex * v->elemSize,
        v->logLen - startIndex, v->elemSize, searchFn);
    }else{
        for(int i = startIndex; i < v->logLen; i++){
            void* curElem = (char*)v->elems + i*v->elemSize;
            if(searchFn(key,curElem) == 0) {
                result = curElem;
                break;
            }
        }
    }
    if(result) return ((char *) result - (char *) v->elems) / v->elemSize;
    return kNotFound;
 } 
