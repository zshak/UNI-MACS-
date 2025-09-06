#include <bits/stdc++.h>
using namespace std;


int testCases;
int curIndex = 0;
int currentBits = 0;
int currentOnes = 0;
int answers[50000];

//segmented sieveის იმპლემენტაცია: https://www.geeksforgeeks.org/segmented-sieve/

void simpleSieve(int limit, vector<int> &prime, vector<pair<int, int>> &queries)
{
    bool mark[100001] = {false};

    for (int p = 2; p < limit; p++)
    {
        if (mark[p] == false)
        {
            int curPrime = p;
            int temp = p;
            int bits = 0;
            while(temp!=0){
                bits++;
                temp/=2;
            }
            int newCurBits = currentBits + bits;
            while (queries[curIndex].first <= newCurBits && curIndex < testCases)
            {
                int dif = queries[curIndex].first - currentBits;
                int ans = currentOnes;
                for (int i = 0; i < dif; i++)
                {
                    ans += (curPrime >> (bits - i - 1)) & 1;
                }
                answers[queries[curIndex].second] = ans;
                curIndex++;
            }
            for (int i = 0; i < bits; i++)
            {
                currentOnes += (curPrime >> (bits - i - 1)) & 1;
            }
            currentBits = newCurBits;
            prime.push_back(p);
            for (int j = p; j < limit; j += p)
                mark[j] = true;
        }
    }
}

void segmentedSieve(int n, vector<pair<int, int>> &queries)
{
    vector<int> prime;
    simpleSieve(100000, prime, queries);

    int low = 100000;
    int high = 200000;

    while (low < n)
    {
        bool mark[100000];
        memset(mark, true, sizeof(mark));
        for (int i = 0; i < prime.size(); i++)
        {
            int loLim = int(low / prime[i]) * prime[i]; 
            if (loLim < low)
                loLim += prime[i];

            for (int j = loLim; j < high; j += prime[i])
                mark[j - low] = false;
        }

        for (int i = low; i < high; i++)
            if (mark[i - low] == true)
            {
                int curPrime = i;
                int bits = 0;
                int temp = i;
                while(temp!=0){
                    bits++;
                    temp/=2;
                }
                int newCurBits = currentBits + bits;
                while (queries[curIndex].first <= newCurBits && curIndex < testCases)
                {
                    int dif = queries[curIndex].first - currentBits;
                    int ans = currentOnes;
                    for (int k = 0; k < dif; k++)
                    {
                        ans += (curPrime >> (bits - k - 1)) & 1;
                    }
                    answers[queries[curIndex].second] = ans;
                    curIndex++;
                }
                for (int k = 0; k < bits; k++)
                {
                    currentOnes += (curPrime >> (bits - k - 1)) & 1;
                }
                currentBits = newCurBits;
            }

        low = low + 100000;
        high = high + 100000;
    }
}

int main()
{
    cin >> testCases;
    int n = 102000000;

    vector<pair<int, int>> queries(testCases);

    for (int i = 0; i < testCases; i++)
    {
        int ind;
        cin >> ind;
        queries[i] = {ind, i}; // {3,0} {10,1}
    }
    sort(queries.begin(), queries.end());

    segmentedSieve(102000000, queries); //https://www.geeksforgeeks.org/segmented-sieve/

    for (int i = 0; i < testCases; i++)
    {
        cout << answers[i] << endl;
    }
    return 0;
}