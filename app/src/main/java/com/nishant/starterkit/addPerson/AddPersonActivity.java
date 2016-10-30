package com.nishant.starterkit.addPerson;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nishant.starterkit.R;
import com.nishant.starterkit.StarterApplication;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class AddPersonActivity extends AppCompatActivity implements AddPersonContract.View {

  public static final int REQUEST_ADD_PERSON = 1;

  @Inject
  AddPersonPresenter mAddPersonPresenter;

  @BindView(R.id.first_name_edit)
  EditText mFirstName;

  @BindView(R.id.last_name_edit)
  EditText mLastName;

  @BindView(R.id.add_person_confirm)
  FloatingActionButton mSubmitButton;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    DaggerAddPersonComponent
      .builder()
      .applicationComponent(((StarterApplication)getApplication()).getComponent())
      .build()
      .inject(this);

    super.onCreate(savedInstanceState);
    setContentView(R.layout.add_person_layout);
    mAddPersonPresenter.attachView(this);
    ButterKnife.bind(this);
  }

  @OnEditorAction(R.id.last_name_edit)
  boolean submitAddPerson(TextView v, int actionId, KeyEvent event) {
    if (EditorInfo.IME_ACTION_DONE == actionId) {
      mSubmitButton.callOnClick();
      return true;
    }
    return false;
  }

  @OnClick(R.id.add_person_confirm)
  void addPerson() {
    mAddPersonPresenter.savePerson(mFirstName.getText().toString(), mLastName.getText().toString());
    mSubmitButton.setClickable(false);
  }



  @Override
  protected void onDestroy() {
    mAddPersonPresenter.detach();
    super.onDestroy();
  }

  @Override
  public void onSuccessfullyAdded() {
    setResult(RESULT_OK);
    finish();
  }

  @Override
  public void failedToAdd(String message) {
    Toast.makeText(this, "failed to load: " + message, Toast.LENGTH_SHORT).show();
  }
}
