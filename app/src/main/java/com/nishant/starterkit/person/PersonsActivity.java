package com.nishant.starterkit.person;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nishant.starterkit.R;
import com.nishant.starterkit.StarterApplication;
import com.nishant.starterkit.addPerson.AddPersonActivity;
import com.nishant.starterkit.data.model.Person;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonsActivity extends RxAppCompatActivity implements PersonsContract.View {

  @Inject
  PersonsPresenter mPersonsPresenter;

  @BindView(R.id.list_view_person)
  RecyclerView mPersonListView;

  @BindView(R.id.add_person)
  FloatingActionButton mAddButton;

  @BindView(R.id.swipe_refresh)
  SwipeRefreshLayout mSwipeRefreshLayout;

  private PersonAdapter mPersonAdapter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    DaggerPersonsComponent
      .builder()
      .applicationComponent(((StarterApplication) getApplication()).getComponent())
      .build().inject(PersonsActivity.this);
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_persons);

    mPersonsPresenter.attachView(this);
    ButterKnife.bind(this);

    mPersonAdapter = new PersonAdapter(new LinkedList<>());
    // use a linear layout manager
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
    mPersonListView.setLayoutManager(mLayoutManager);

    mPersonListView.setHasFixedSize(true);
    mPersonListView.setAdapter(mPersonAdapter);

    mSwipeRefreshLayout.setOnRefreshListener(() -> mPersonsPresenter.getPersons(true));
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @OnClick(R.id.add_person)
  void addPerson() {
    Intent intent = new Intent(this, AddPersonActivity.class);
    startActivityForResult(intent, AddPersonActivity.REQUEST_ADD_PERSON);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (RESULT_OK == resultCode) {
      if (AddPersonActivity.REQUEST_ADD_PERSON == requestCode) {
        mPersonsPresenter.getPersons(false);
      }
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  protected void onRestart() {
    super.onRestart();
  }

  @Override
  protected void onDestroy() {
    mPersonsPresenter.detach();
    super.onDestroy();
  }

  @Override
  public void showPersons(List<Person> persons) {
    mPersonAdapter.replaceData(persons);
    mSwipeRefreshLayout.setRefreshing(false);
  }
}
