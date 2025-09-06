#include<bits/stdc++.h>
using namespace std;
 
//hint taken from : https://codeforces.com/gym/103860/attachments/download/17128/CCPC-Finals-2021-Tutorial.pdf
 
const int mod = 998244353;
const int max_length = 200005;
long long fact[max_length];
long long rev_fact[max_length];
int seq[max_length];
int n;
 
void init_fact_rec(int ind){
    if(ind == max_length) {
        cout<<ind<<endl;
        return;
    }
    fact[ind] = fact[ind - 1] * ind % mod;
    init_fact_rec(ind + 1);
}
 
long long fast_pow(long long a, long long b){
    if(b == 0) return 1;
 
    return (b%2 == 0) ? fast_pow(a*a%mod,b/2)%mod : a*fast_pow(a*a%mod,b/2)%mod;
}
 
void initFact(){
    fact[0] = 1;
    for(int i = 1; i < max_length; i ++ ){
        fact[i] = fact[i-1] * i % mod;
    }
}
 
 
void init_rev_fact_rec(int ind){
    if(ind == 0) return;
    rev_fact[ind - 1] = rev_fact[ind] * ind % mod;
    init_rev_fact_rec(ind - 1);
}
 
void initRevFact(){
    rev_fact[0] = 1;
    long long temp = fact[max_length - 1];
    long long temp2 = mod - 2;
    rev_fact[max_length - 1] = fast_pow(temp,temp2);
    for(int i = max_length - 2; i >= 0; i -- ){
        rev_fact[i] = rev_fact[i + 1]* (i+1) % mod;
    }
}
 
long long calculate(int i){
    long long temp1 = seq[i] + 1;
    long long fact_of_temp1 = fact[temp1];
    long long temp2 = seq[i - 1];
    long long rev_of_temp2 = rev_fact[temp2];
    long long temp3 = temp1 - temp2;
    return fact_of_temp1 * rev_of_temp2 %mod * rev_fact[temp3] %mod;
}
 
int main(){
    int n;
    cin>>n;
    initFact();
    initRevFact();
 
    for(int i = 1; i < n; i++){
        cin>>seq[i];
    }
 
    long long res = 1;
    for(int i = 2; i < n; i ++ ){
        res = res* calculate(i);
        res = res%mod;
    }
    cout<<res<<endl;
}
