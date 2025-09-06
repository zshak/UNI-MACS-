#include "hashset.h"
#include <assert.h>
#include <stdlib.h>
#include <string.h>

void HashSetNew(hashset *h, int elemSize, int numBuckets,
		HashSetHashFunction hashfn, HashSetCompareFunction comparefn, HashSetFreeFunction freefn)
{
	assert(elemSize > 0 && numBuckets > 0 && hashfn && comparefn);
	h->elemSize = elemSize;
	h->numBuckets = numBuckets;
	h->hashfn = hashfn;
	h->comparefn = comparefn;
	h->freefn = freefn;
	h->logLen = 0;
	h->elems = malloc(numBuckets * sizeof(vector));
	assert(h->elems);
	for(int i = 0; i < numBuckets; i++){
		vector cur;
		VectorNew(&cur, elemSize, freefn, 4);
		h->elems[i] = cur;
	}
}

void HashSetDispose(hashset *h)
{
	for(int i = 0; i < h->numBuckets; i++){
		VectorDispose(h->elems + i);
	}
	free(h->elems);
}

int HashSetCount(const hashset *h)
{
	 return h->logLen; 
}

void HashSetMap(hashset *h, HashSetMapFunction mapfn, void *auxData)
{
	assert(mapfn);
	for(int i = 0; i < h->numBuckets; i++){
		VectorMap(h->elems + i, mapfn, auxData);
	}
}

void HashSetEnter(hashset *h, const void *elemAddr)
{
	assert(elemAddr);
	int bucketIndex = h->hashfn(elemAddr, h->numBuckets);
	assert(bucketIndex >=0 && bucketIndex < h->numBuckets);

	int index = VectorSearch(h->elems + bucketIndex,elemAddr,
		h->comparefn, 0, false);

	if(index == -1){
		VectorAppend(h->elems+bucketIndex, elemAddr);
		h->logLen++;
	}else{
		VectorReplace(h->elems+bucketIndex, elemAddr, index);
	}	
}

void *HashSetLookup(const hashset *h, const void *elemAddr)
 { 
	assert(elemAddr);
	int bucketIndex = h->hashfn(elemAddr, h->numBuckets);
	assert(bucketIndex >=0 && bucketIndex < h->numBuckets);

	int index = VectorSearch(h->elems+bucketIndex,elemAddr,
		h->comparefn, 0, false);
	
	if(index == -1) return NULL;
	return VectorNth(h->elems+bucketIndex, index);
 }
