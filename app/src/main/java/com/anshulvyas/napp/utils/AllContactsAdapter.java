//package com.anshulvyas.napp.utils;
//
///**
// * Created by av7 on 1/25/17.
// */
//
//import android.app.ListActivity;
//import android.content.Context;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import java.util.List;
//
//public class AllContactsAdapter extends BaseAdapter {
//
//    private List<ContactsDAO> contactsDAOList;
//    private Context mContext;
//    public AllContactsAdapter(List<ContactsDAO> contactVOList, Context mContext){
//        this.contactsDAOList = contactVOList;
//        this.mContext = mContext;
//    }
//
//    @Override
//    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.single_contact_view, null);
//        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
//        return contactViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(ContactViewHolder holder, int position) {
//        ContactsDAO contactsDAO = contactsDAOList.get(position);
//        holder.tvContactName.setText(contactsDAO.getContactName());
//        holder.tvPhoneNumber.setText(contactsDAO.getContactNumber());
//    }
//
//    @Override
//    public int getItemCount() {
//        return contactsDAOList.size();
//    }
//
//    public static class ContactViewHolder extends RecyclerView.ViewHolder{
//
//        ImageView ivContactImage;
//        TextView tvContactName;
//        TextView tvPhoneNumber;
//
//        public ContactViewHolder(View itemView) {
//            super(itemView);
//            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
//            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
//            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
//        }
//    }
//}
