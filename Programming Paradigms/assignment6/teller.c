#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <assert.h>
#include <inttypes.h>

#include "teller.h"
#include "account.h"
#include "error.h"
#include "debug.h"
#include "account.c"
/*
 * deposit money into an account
 */
int
Teller_DoDeposit(Bank *bank, AccountNumber accountNum, AccountAmount amount)
{
  assert(amount >= 0);

  Account *account = Account_LookupByNumber(bank, accountNum);
  int branchId = AccountNum_GetBranchID(accountNum);
  sem_wait(&(bank->branches[branchId].branch_lock));
  sem_wait(&(account->account_lock));  

  DPRINTF('t', ("Teller_DoDeposit(account 0x%"PRIx64" amount %"PRId64")\n",
                accountNum, amount));


  if (account == NULL) {
    sem_post(&(account->account_lock));  
    sem_post(&(bank->branches[branchId].branch_lock));
    return ERROR_ACCOUNT_NOT_FOUND;
  }

  Account_Adjust(bank,account, amount, 1);
  sem_post(&(account->account_lock));  
  sem_post(&(bank->branches[branchId].branch_lock));
  return ERROR_SUCCESS;
}

/*
 * withdraw money from an account
 */
int
Teller_DoWithdraw(Bank *bank, AccountNumber accountNum, AccountAmount amount)
{
  assert(amount >= 0);




  Account *account = Account_LookupByNumber(bank, accountNum);
  int branchId = AccountNum_GetBranchID(accountNum);
  sem_wait(&(bank->branches[branchId].branch_lock));
  sem_wait(&(account->account_lock));  

  DPRINTF('t', ("Teller_DoWithdraw(account 0x%"PRIx64" amount %"PRId64")\n",
            accountNum, amount));

  if (account == NULL) {
    sem_post(&(account->account_lock));  
    sem_post(&(bank->branches[branchId].branch_lock));
    return ERROR_ACCOUNT_NOT_FOUND;
  }


  if (amount > Account_Balance(account)) {
    sem_post(&(account->account_lock));  
    sem_post(&(bank->branches[branchId].branch_lock));
    return ERROR_INSUFFICIENT_FUNDS;
  }

  Account_Adjust(bank,account, -amount, 1);

  sem_post(&(account->account_lock));  
  sem_post(&(bank->branches[branchId].branch_lock));
  return ERROR_SUCCESS;
}

/*
 * do a tranfer from one account to another account
 */
int
Teller_DoTransfer(Bank *bank, AccountNumber srcAccountNum,
                  AccountNumber dstAccountNum,
                  AccountAmount amount)
{
  assert(amount >= 0);

  DPRINTF('t', ("Teller_DoTransfer(src 0x%"PRIx64", dst 0x%"PRIx64
                ", amount %"PRId64")\n",
                srcAccountNum, dstAccountNum, amount));

  Account *srcAccount = Account_LookupByNumber(bank, srcAccountNum);
  

  if (srcAccount == NULL) {
    return ERROR_ACCOUNT_NOT_FOUND;
  }

  Account *dstAccount = Account_LookupByNumber(bank, dstAccountNum);

  if (dstAccount == NULL) {
    return ERROR_ACCOUNT_NOT_FOUND;
  }

  if(srcAccount == dstAccount) {
    return ERROR_SUCCESS;
  }

  int sameBranch = Account_IsSameBranch(srcAccountNum, dstAccountNum);
  if(sameBranch){
    if(srcAccount->accountNumber < dstAccount->accountNumber) {   
      sem_wait(&(srcAccount->account_lock));                        
      sem_wait(&(dstAccount->account_lock));
    } else {
      sem_wait(&(dstAccount->account_lock));                          
      sem_wait(&(srcAccount->account_lock));
    }

    if (amount > Account_Balance(srcAccount)) {
      sem_post(&(dstAccount->account_lock));                          
      sem_post(&(srcAccount->account_lock));
      return ERROR_INSUFFICIENT_FUNDS;
    }

    /*
    * If we are doing a transfer within the branch, we tell the Account module to
    * not bother updating the branch balance since the net change for the
    * branch is 0.
    */
    int updateBranch = !Account_IsSameBranch(srcAccountNum, dstAccountNum);

    Account_Adjust(bank, srcAccount, -amount, updateBranch);
    Account_Adjust(bank, dstAccount, amount, updateBranch);
    sem_post(&(dstAccount->account_lock));                          
    sem_post(&(srcAccount->account_lock));
    return ERROR_SUCCESS;
  }else{
    int src_Branch_Id = AccountNum_GetBranchID(srcAccountNum);
    int dst_Branch_Id = AccountNum_GetBranchID(dstAccountNum);
    if (src_Branch_Id < dst_Branch_Id) {              
      sem_wait(&(bank->branches[src_Branch_Id].branch_lock));   
      sem_wait(&(bank->branches[dst_Branch_Id].branch_lock));
      sem_wait(&(srcAccount->account_lock));            
      sem_wait(&(dstAccount->account_lock));         
    } else {
      sem_wait(&(bank->branches[dst_Branch_Id].branch_lock));
      sem_wait(&(bank->branches[src_Branch_Id].branch_lock));
      sem_wait(&(dstAccount->account_lock));
      sem_wait(&(srcAccount->account_lock));
    }

    if (amount > Account_Balance(srcAccount)) {
      sem_post(&(dstAccount->account_lock));                          
      sem_post(&(srcAccount->account_lock));
      sem_post(&(bank->branches[dst_Branch_Id].branch_lock));
      sem_post(&(bank->branches[src_Branch_Id].branch_lock));
      return ERROR_INSUFFICIENT_FUNDS;
    }
    int updateBranch = !Account_IsSameBranch(srcAccountNum, dstAccountNum);

    Account_Adjust(bank, srcAccount, -amount, updateBranch);
    Account_Adjust(bank, dstAccount, amount, updateBranch);
    sem_post(&(dstAccount->account_lock));                          
    sem_post(&(srcAccount->account_lock));
    sem_post(&(bank->branches[dst_Branch_Id].branch_lock));
    sem_post(&(bank->branches[src_Branch_Id].branch_lock));
    return ERROR_SUCCESS;
  }

}
