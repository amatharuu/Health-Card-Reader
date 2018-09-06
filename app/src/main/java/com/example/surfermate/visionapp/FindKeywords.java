package com.example.surfermate.visionapp;

import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindKeywords {

    /*this class takes the information from the MainActivity, analyzes the important keyword. Differentiates
    name and address from health card. Stores it in Database. Which will be accessed in a different activity.
     */

    TextView reviewText;
    TextView reviewDOB;


    //private String infoWithIntent;

    public FindKeywords(String infoWithIntent){

       // this.infoWithIntent = infoWithIntent;
        this.reviewText = reviewText;
        this.reviewDOB = reviewDOB;

        ///findName(infoWithIntent);
        //findDOB(infoWithIntent);


    }

    //split the string and use the appropriate content
    public void findName(String infoWithIntent, TextView reviewText){
        try{
            //captures data and shows it to the user boii

            String replaceWith = "[^a-zA-Z]+";
            String replaced = infoWithIntent.replaceAll(replaceWith, " ");

            String[] splitReplace = replaced.split(" ");
            List<String> itemWords = Arrays.asList(splitReplace);
            //need to figure out how to iterate through each element in list.


            if(itemWords.contains("Health")){
                for(int i = 2; i < itemWords.size(); i++) {
                    if (itemWords.get(i).length() > 2) {

                        reviewText.append(itemWords.get(i));
                        reviewText.append("\n");
                    }
                }
            }else{
                reviewText.setText("Keyword Health is missing");
            }

        } catch(IllegalArgumentException ie){
            throw ie;
        }
    }

    //method to find date of birth
    public void findDOB(String infoWithIntent, TextView reviewDOB){
            try{
                String replacedWith = "[^0-9]+";
                String replaceThis = infoWithIntent.replaceAll(replacedWith, " ");

                String[] splitText = replaceThis.split(" ");
                List<String> itemWords = Arrays.asList(splitText);

                //have to figure out how to get only the dob
                for(int i = 0; i < itemWords.size(); i++){
                    if(itemWords.get(i).length() == 4) {
                        reviewDOB.append(itemWords.get(i));
                    }
                    if(itemWords.get(i).length() ==2){
                        reviewDOB.append((itemWords.get(i)));
                    }
                    //}
                }
            }catch(IllegalArgumentException ie){
                throw ie;
            }
    }


}
