package com.example.maximus.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maximus.myapplication.model.GitHubModel;

import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private List<GitHubModel> gitHubModelList;
    private String[] jsonData;


    public RecyclerAdapter(List<GitHubModel> gitHubModels) {
        this.gitHubModelList = gitHubModels;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final GitHubModel gitHubModel = gitHubModelList.get(position);
//        String jsonDescription = jsonData[position];
        holder.repoDescription.setText(gitHubModel.getRepoDescription());
        holder.repoName.setText(gitHubModel.getRepoName());
        holder.repoStars.setText(gitHubModel.getStarsCount());


    }

    @Override
    public int getItemCount() {
        if (gitHubModelList == null) {
            return 0;
        }
        return gitHubModelList.size();
    }


    public void setJsonData(List<GitHubModel> gitHubModels) {
        this.gitHubModelList = gitHubModels;
        notifyDataSetChanged();
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView repoDescription;
        TextView repoName;
        TextView repoStars;


        public MyViewHolder(View itemView) {
            super(itemView);
            repoDescription = itemView.findViewById(R.id.repo_description);
            repoName = itemView.findViewById(R.id.repo_name);
            repoStars = itemView.findViewById(R.id.number_of_stars);


        }




    }
}
