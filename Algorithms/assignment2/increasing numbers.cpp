#include <bits/stdc++.h>

using namespace std;

//https://www.geeksforgeeks.org/longest-monotonically-increasing-subsequence-size-n-log-n/ 
//longest increasing subsequence 
//c++(gcc 8.3)

int CeilIndex(vector<int> &v, int l, int r, int key)
{
    while (r - l > 1)
    {
        int m = l + (r - l) / 2;
        if (v[m] >= key)
            r = m;
        else
            l = m;
    }

    return r;
}

int LongestIncreasingSubsequenceLength(vector<int>& v, int size, vector<int>& tail)
{
    if (size == 0)
        return 0;


    int length = 1;

    tail[0] = v[0];
    for (size_t i = 1; i < size; i++)
    {

        if (v[i] < tail[0])
            tail[0] = v[i];

        else if (v[i] > tail[length - 1])
            tail[length++] = v[i];

        else
            tail[CeilIndex(tail, -1, length - 1, v[i])] = v[i];
    }

    return length;
}

int main()
{
    int testCases;
    cin >> testCases;
    for(int i = 0; i < testCases; i++){
        int size;
        cin>>size;
        vector<int> vec(size);
        vec.reserve(size);
        for(int j = 0; j < size; j++){
            cin>>vec[j];
        }
        int seqLen;
        cin>>seqLen;

        vector<int> tail(size, 0);
        int longestSeq = LongestIncreasingSubsequenceLength(vec,size,tail);
        if(longestSeq < seqLen) {
            cout<<"-1"<<endl;
        }else{
            cout<<tail[seqLen - 1]<<endl;
        }
    }
    return 0;
}