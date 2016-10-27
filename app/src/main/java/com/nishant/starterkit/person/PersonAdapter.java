package com.nishant.starterkit.person;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nishant.starterkit.R;
import com.nishant.starterkit.data.model.Person;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

  List<Person> persons;

  public PersonAdapter(List<Person> persons) {
    this.persons = persons;
  }

  public void setPersons(@NonNull List<Person> persons) {
    this.persons = persons;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.id.setText(String.valueOf(persons.get(position)._id()));
    holder.fistName.setText(persons.get(position).first_name());
    holder.lastName.setText(persons.get(position).last_name());
  }

  @Override
  public int getItemCount() {
    return persons.size();
  }

  public void replaceData(List<Person> persons) {
    setPersons(persons);
    notifyDataSetChanged();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.id)
    TextView id;

    @BindView(R.id.first_name)
    TextView fistName;

    @BindView(R.id.last_name)
    TextView lastName;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
