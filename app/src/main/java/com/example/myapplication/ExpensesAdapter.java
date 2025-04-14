package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ExpensesViewHolder> {

    private List<Expense> expenseList;
    private Context context;

    public ExpensesAdapter(Context context, List<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_list_item, parent, false);
        return new ExpensesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Expense expense = expenseList.get(position);
        holder.titleTextView.setText(expense.getExpenseTitle());
        holder.dateTextView.setText(expense.getDate());
        holder.amountTextView.setText(String.format("RM %s", expense.getAmount()));

        holder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ExpenseDetailActivity.class);
                intent.putExtra("expenseId", expense.getId());
                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHandler dbHandler = new DBHandler(context);
                dbHandler.deleteExpense(expense.getId());

                // Refresh the list from database
                expenseList.clear();
                expenseList.addAll(dbHandler.getAllExpensesAsList());
                notifyDataSetChanged();

                Toast.makeText(context, "Expense deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ExpensesViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        TextView amountTextView;
        Button deleteButton;

        public ExpensesViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textView_expense_title);
            dateTextView = itemView.findViewById(R.id.textView_expense_date);
            amountTextView = itemView.findViewById(R.id.textView_expense_amount);
            deleteButton = itemView.findViewById(R.id.button_delete_expense);
        }
    }
}
