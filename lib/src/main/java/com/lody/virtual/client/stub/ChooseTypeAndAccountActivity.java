package com.lody.virtual.client.stub;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorDescription;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.lody.virtual.R;
import com.lody.virtual.client.ipc.VAccountManager;
import com.lody.virtual.helper.utils.VLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
public class ChooseTypeAndAccountActivity extends Activity
        implements AccountManagerCallback<Bundle> {
    private static final String TAG = "AccountChooser";
    public static final String EXTRA_ALLOWABLE_ACCOUNTS_ARRAYLIST = "allowableAccounts";
    public static final String EXTRA_ALLOWABLE_ACCOUNT_TYPES_STRING_ARRAY = "allowableAccountTypes";
    public static final String EXTRA_ADD_ACCOUNT_OPTIONS_BUNDLE = "addAccountOptions";
    public static final String EXTRA_ADD_ACCOUNT_REQUIRED_FEATURES_STRING_ARRAY =
            "addAccountRequiredFeatures";
    public static final String EXTRA_ADD_ACCOUNT_AUTH_TOKEN_TYPE_STRING = "authTokenType";
    public static final String EXTRA_SELECTED_ACCOUNT = "selectedAccount";
    @Deprecated
    public static final String EXTRA_ALWAYS_PROMPT_FOR_ACCOUNT =
            "alwaysPromptForAccount";
    public static final String EXTRA_DESCRIPTION_TEXT_OVERRIDE =
            "descriptionTextOverride";
    public static final int REQUEST_NULL = 0;
    public static final int REQUEST_CHOOSE_TYPE = 1;
    public static final int REQUEST_ADD_ACCOUNT = 2;
    private static final String KEY_INSTANCE_STATE_PENDING_REQUEST = "pendingRequest";
    private static final String KEY_INSTANCE_STATE_EXISTING_ACCOUNTS = "existingAccounts";
    private static final String KEY_INSTANCE_STATE_SELECTED_ACCOUNT_NAME = "selectedAccountName";
    private static final String KEY_INSTANCE_STATE_SELECTED_ADD_ACCOUNT = "selectedAddAccount";
    private static final String KEY_INSTANCE_STATE_ACCOUNT_LIST = "accountList";
    public static final String KEY_USER_ID = "userId";
    private static final int SELECTED_ITEM_NONE = -1;
    private Set<Account> mSetOfAllowableAccounts;
    private Set<String> mSetOfRelevantAccountTypes;
    private String mSelectedAccountName = null;
    private boolean mSelectedAddNewAccount = false;
    private String mDescriptionOverride;
    private ArrayList<Account> mAccounts;
    private int mPendingRequest = REQUEST_NULL;
    private Parcelable[] mExistingAccounts = null;
    private int mSelectedItemIndex;
    private Button mOkButton;
    private int mCallingUserId;
    private boolean mDontShowPicker;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        final Intent intent = getIntent();
        if (savedInstanceState != null) {
            mPendingRequest = savedInstanceState.getInt(KEY_INSTANCE_STATE_PENDING_REQUEST);
            mExistingAccounts =
                    savedInstanceState.getParcelableArray(KEY_INSTANCE_STATE_EXISTING_ACCOUNTS);
            mSelectedAccountName = savedInstanceState.getString(
                    KEY_INSTANCE_STATE_SELECTED_ACCOUNT_NAME);
            mSelectedAddNewAccount = savedInstanceState.getBoolean(
                    KEY_INSTANCE_STATE_SELECTED_ADD_ACCOUNT, false);
            mAccounts = savedInstanceState.getParcelableArrayList(KEY_INSTANCE_STATE_ACCOUNT_LIST);
            mCallingUserId = savedInstanceState.getInt(KEY_USER_ID);
        } else {
            mPendingRequest = REQUEST_NULL;
            mExistingAccounts = null;
            mCallingUserId = intent.getIntExtra(KEY_USER_ID, -1);
            Account selectedAccount = intent.getParcelableExtra(EXTRA_SELECTED_ACCOUNT);
            if (selectedAccount != null) {
                mSelectedAccountName = selectedAccount.name;
            }
        }
        VLog.v(TAG, "selected account name is " + mSelectedAccountName);
        mSetOfAllowableAccounts = getAllowableAccountSet(intent);
        mSetOfRelevantAccountTypes = getReleventAccountTypes(intent);
        mDescriptionOverride = intent.getStringExtra(EXTRA_DESCRIPTION_TEXT_OVERRIDE);
        mAccounts = getAcceptableAccountChoices(VAccountManager.get());
        if (mDontShowPicker) {
            super.onCreate(savedInstanceState);
            return;
        }
        if (mPendingRequest == REQUEST_NULL) {
            if (mAccounts.isEmpty()) {
                setNonLabelThemeAndCallSuperCreate(savedInstanceState);
                if (mSetOfRelevantAccountTypes.size() == 1) {
                    runAddAccountForAuthenticator(mSetOfRelevantAccountTypes.iterator().next());
                } else {
                    startChooseAccountTypeActivity();
                }
            }
        }
        String[] listItems = getListOfDisplayableOptions(mAccounts);
        mSelectedItemIndex = getItemIndexToSelect(
                mAccounts, mSelectedAccountName, mSelectedAddNewAccount);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_type_and_account);
        overrideDescriptionIfSupplied(mDescriptionOverride);
        populateUIAccountList(listItems);
        mOkButton = (Button) findViewById(android.R.id.button2);
        mOkButton.setEnabled(mSelectedItemIndex != SELECTED_ITEM_NONE);
    }
    @Override
    protected void onDestroy() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "ChooseTypeAndAccountActivity.onDestroy()");
        }
        super.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INSTANCE_STATE_PENDING_REQUEST, mPendingRequest);
        if (mPendingRequest == REQUEST_ADD_ACCOUNT) {
            outState.putParcelableArray(KEY_INSTANCE_STATE_EXISTING_ACCOUNTS, mExistingAccounts);
        }
        if (mSelectedItemIndex != SELECTED_ITEM_NONE) {
            if (mSelectedItemIndex == mAccounts.size()) {
                outState.putBoolean(KEY_INSTANCE_STATE_SELECTED_ADD_ACCOUNT, true);
            } else {
                outState.putBoolean(KEY_INSTANCE_STATE_SELECTED_ADD_ACCOUNT, false);
                outState.putString(KEY_INSTANCE_STATE_SELECTED_ACCOUNT_NAME,
                        mAccounts.get(mSelectedItemIndex).name);
            }
        }
        outState.putParcelableArrayList(KEY_INSTANCE_STATE_ACCOUNT_LIST, mAccounts);
    }
    public void onCancelButtonClicked(View view) {
        onBackPressed();
    }
    public void onOkButtonClicked(View view) {
        if (mSelectedItemIndex == mAccounts.size()) {
            startChooseAccountTypeActivity();
        } else if (mSelectedItemIndex != SELECTED_ITEM_NONE) {
            onAccountSelected(mAccounts.get(mSelectedItemIndex));
        }
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            if (data != null && data.getExtras() != null) data.getExtras().keySet();
            Bundle extras = data != null ? data.getExtras() : null;
            Log.v(TAG, "ChooseTypeAndAccountActivity.onActivityResult(reqCode=" + requestCode
                    + ", resCode=" + resultCode + ", extras=" + extras + ")");
        }
        mPendingRequest = REQUEST_NULL;
        if (resultCode == RESULT_CANCELED) {
            if (mAccounts.isEmpty()) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
            return;
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_TYPE) {
                if (data != null) {
                    String accountType = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
                    if (accountType != null) {
                        runAddAccountForAuthenticator(accountType);
                        return;
                    }
                }
                Log.d(TAG, "ChooseTypeAndAccountActivity.onActivityResult: unable to find account "
                        + "type, pretending the request was canceled");
            } else if (requestCode == REQUEST_ADD_ACCOUNT) {
                String accountName = null;
                String accountType = null;
                if (data != null) {
                    accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    accountType = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
                }
                if (accountName == null || accountType == null) {
                    Account[] currentAccounts = VAccountManager.get().getAccounts(mCallingUserId, null);
                    Set<Account> preExistingAccounts = new HashSet<>();
                    for (Parcelable accountParcel : mExistingAccounts) {
                        preExistingAccounts.add((Account) accountParcel);
                    }
                    for (Account account : currentAccounts) {
                        if (!preExistingAccounts.contains(account)) {
                            accountName = account.name;
                            accountType = account.type;
                            break;
                        }
                    }
                }
                if (accountName != null || accountType != null) {
                    setResultAndFinish(accountName, accountType);
                    return;
                }
            }
            Log.d(TAG, "ChooseTypeAndAccountActivity.onActivityResult: unable to find added "
                    + "account, pretending the request was canceled");
        }
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "ChooseTypeAndAccountActivity.onActivityResult: canceled");
        }
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
    protected void runAddAccountForAuthenticator(String type) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "runAddAccountForAuthenticator: " + type);
        }
        final Bundle options = getIntent().getBundleExtra(
                ChooseTypeAndAccountActivity.EXTRA_ADD_ACCOUNT_OPTIONS_BUNDLE);
        final String[] requiredFeatures = getIntent().getStringArrayExtra(
                ChooseTypeAndAccountActivity.EXTRA_ADD_ACCOUNT_REQUIRED_FEATURES_STRING_ARRAY);
        final String authTokenType = getIntent().getStringExtra(
                ChooseTypeAndAccountActivity.EXTRA_ADD_ACCOUNT_AUTH_TOKEN_TYPE_STRING);
        VAccountManager.get().addAccount(mCallingUserId, type, authTokenType, requiredFeatures,
                options, null /* activity */, this /* callback */, null /* Handler */);
    }
    @Override
    public void run(final AccountManagerFuture<Bundle> accountManagerFuture) {
        try {
            final Bundle accountManagerResult = accountManagerFuture.getResult();
            final Intent intent = accountManagerResult.getParcelable(
                    AccountManager.KEY_INTENT);
            if (intent != null) {
                mPendingRequest = REQUEST_ADD_ACCOUNT;
                mExistingAccounts = VAccountManager.get().getAccounts(mCallingUserId, null);
                intent.setFlags(intent.getFlags() & ~Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, REQUEST_ADD_ACCOUNT);
                return;
            }
        } catch (OperationCanceledException e) {
            setResult(Activity.RESULT_CANCELED);
            finish();
            return;
        } catch (IOException e) {
        } catch (AuthenticatorException e) {
        }
        Bundle bundle = new Bundle();
        bundle.putString(AccountManager.KEY_ERROR_MESSAGE, "error communicating with server");
        setResult(Activity.RESULT_OK, new Intent().putExtras(bundle));
        finish();
    }
    private void setNonLabelThemeAndCallSuperCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTheme(android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        } else {
            setTheme(android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        }
        super.onCreate(savedInstanceState);
    }
    private void onAccountSelected(Account account) {
        Log.d(TAG, "selected account " + account);
        setResultAndFinish(account.name, account.type);
    }
    private void setResultAndFinish(final String accountName, final String accountType) {
        Bundle bundle = new Bundle();
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, accountName);
        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        setResult(Activity.RESULT_OK, new Intent().putExtras(bundle));
        VLog.v(TAG, "ChooseTypeAndAccountActivity.setResultAndFinish: "
                + "selected account " + accountName + ", " + accountType);
        finish();
    }
    private void startChooseAccountTypeActivity() {
        VLog.v(TAG, "ChooseAccountTypeActivity.startChooseAccountTypeActivity()");
        final Intent intent = new Intent(this, ChooseAccountTypeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(EXTRA_ALLOWABLE_ACCOUNT_TYPES_STRING_ARRAY,
                getIntent().getStringArrayExtra(EXTRA_ALLOWABLE_ACCOUNT_TYPES_STRING_ARRAY));
        intent.putExtra(EXTRA_ADD_ACCOUNT_OPTIONS_BUNDLE,
                getIntent().getBundleExtra(EXTRA_ADD_ACCOUNT_OPTIONS_BUNDLE));
        intent.putExtra(EXTRA_ADD_ACCOUNT_REQUIRED_FEATURES_STRING_ARRAY,
                getIntent().getStringArrayExtra(EXTRA_ADD_ACCOUNT_REQUIRED_FEATURES_STRING_ARRAY));
        intent.putExtra(EXTRA_ADD_ACCOUNT_AUTH_TOKEN_TYPE_STRING,
                getIntent().getStringExtra(EXTRA_ADD_ACCOUNT_AUTH_TOKEN_TYPE_STRING));
        startActivityForResult(intent, REQUEST_CHOOSE_TYPE);
        mPendingRequest = REQUEST_CHOOSE_TYPE;
    }
    private int getItemIndexToSelect(ArrayList<Account> accounts, String selectedAccountName,
                                     boolean selectedAddNewAccount) {
        if (selectedAddNewAccount) {
            return accounts.size();
        }
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).name.equals(selectedAccountName)) {
                return i;
            }
        }
        return SELECTED_ITEM_NONE;
    }
    private String[] getListOfDisplayableOptions(ArrayList<Account> accounts) {
        String[] listItems = new String[accounts.size() + 1];
        for (int i = 0; i < accounts.size(); i++) {
            listItems[i] = accounts.get(i).name;
        }
        listItems[accounts.size()] = getResources().getString(
                R.string.add_account_button_label);
        return listItems;
    }
    private ArrayList<Account> getAcceptableAccountChoices(VAccountManager accountManager) {
        final Account[] accounts = accountManager.getAccounts(mCallingUserId, null);
        ArrayList<Account> accountsToPopulate = new ArrayList<>(accounts.length);
        for (Account account : accounts) {
            if (mSetOfAllowableAccounts != null && !mSetOfAllowableAccounts.contains(account)) {
                continue;
            }
            if (mSetOfRelevantAccountTypes != null
                    && !mSetOfRelevantAccountTypes.contains(account.type)) {
                continue;
            }
            accountsToPopulate.add(account);
        }
        return accountsToPopulate;
    }
    private Set<String> getReleventAccountTypes(final Intent intent) {
        Set<String> setOfRelevantAccountTypes;
        final String[] allowedAccountTypes =
                intent.getStringArrayExtra(EXTRA_ALLOWABLE_ACCOUNT_TYPES_STRING_ARRAY);
        AuthenticatorDescription[] descs = VAccountManager.get().getAuthenticatorTypes();
        Set<String> supportedAccountTypes = new HashSet<String>(descs.length);
        for (AuthenticatorDescription desc : descs) {
            supportedAccountTypes.add(desc.type);
        }
        if (allowedAccountTypes != null) {
            setOfRelevantAccountTypes = new HashSet<>();
            Collections.addAll(setOfRelevantAccountTypes, allowedAccountTypes);
            setOfRelevantAccountTypes.retainAll(supportedAccountTypes);
        } else {
            setOfRelevantAccountTypes = supportedAccountTypes;
        }
        return setOfRelevantAccountTypes;
    }
    private Set<Account> getAllowableAccountSet(final Intent intent) {
        Set<Account> setOfAllowableAccounts = null;
        final ArrayList<Parcelable> validAccounts =
                intent.getParcelableArrayListExtra(EXTRA_ALLOWABLE_ACCOUNTS_ARRAYLIST);
        if (validAccounts != null) {
            setOfAllowableAccounts = new HashSet<>(validAccounts.size());
            for (Parcelable parcelable : validAccounts) {
                setOfAllowableAccounts.add((Account) parcelable);
            }
        }
        return setOfAllowableAccounts;
    }
    private void overrideDescriptionIfSupplied(String descriptionOverride) {
        TextView descriptionView = (TextView) findViewById(R.id.description);
        if (!TextUtils.isEmpty(descriptionOverride)) {
            descriptionView.setText(descriptionOverride);
        } else {
            descriptionView.setVisibility(View.GONE);
        }
    }
    private void populateUIAccountList(String[] listItems) {
        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice, listItems));
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setItemsCanFocus(false);
        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        mSelectedItemIndex = position;
                        mOkButton.setEnabled(true);
                    }
                });
        if (mSelectedItemIndex != SELECTED_ITEM_NONE) {
            list.setItemChecked(mSelectedItemIndex, true);
            VLog.v(TAG, "List item " + mSelectedItemIndex + " should be selected");
        }
    }
}
