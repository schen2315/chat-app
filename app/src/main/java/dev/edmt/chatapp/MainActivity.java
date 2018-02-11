package dev.edmt.chatapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.text.format.DateFormat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    //private FirebaseListAdapter<ChatMessage> adapter;
    ArrayAdapter<String> itemsAdapter;
    private ArrayList<String> messages;
    private String credentials = "9bed217f0bc146c897f164f5f61acf00";
    ListView listOfMessage;
    Catergories cat;
    RelativeLayout activity_main;

    String [] phrases = {   "Check my finances.",
            "I bought a thing.",
            "Create a budget.",
    };
    String [] responses = {
            "Check your finances?",
            "Input spending amount?",
            "Start a budget?"
    };

    //Add Emojicon
    EmojiconEditText emojiconEditText;
    ImageView emojiButton,submitButton;
    EmojIconActions emojIconActions;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out)
        {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activity_main,"You have been signed out.", Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                Snackbar.make(activity_main,"Successfully signed in.Welcome!", Snackbar.LENGTH_SHORT).show();
                displayChatMessage();
            }
            else{
                Snackbar.make(activity_main,"We couldn't sign you in.Please try again later", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main = (RelativeLayout)findViewById(R.id.activity_main);
        messages = new ArrayList<String>();
        cat = new Catergories();

        //Add Emoji
        emojiButton = (ImageView)findViewById(R.id.emoji_button);
        submitButton = (ImageView)findViewById(R.id.submit_button);
        emojiconEditText = (EmojiconEditText)findViewById(R.id.emojicon_edit_text);
        emojIconActions = new EmojIconActions(getApplicationContext(),activity_main,emojiButton,emojiconEditText);
        emojIconActions.ShowEmojicon();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(emojiconEditText.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));*/
                String input = emojiconEditText.getText().toString();
                messages.add(input);
                emojiconEditText.setText("");
                emojiconEditText.requestFocus();
                itemsAdapter.notifyDataSetChanged();
                listOfMessage.setSelection(itemsAdapter.getCount()-1);
                //final ArrayList<JSONObject> similarities = new ArrayList<JSONObject>();
                //String url= "https://api.dandelion.eu/datatxt/sim/v1/?text1=Cameron%20wins%20the%20Oscar &text2=All%20nominees%20for%20the%20Academy%20Awards&token=" + credentials;
                //String url = "https://my-json-feed";
                //String url = "https://jsonplaceholder.typicode.com/posts/1"
                /*
                for(int i=0; i < phrases.length ; i++) {
                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("https")
                            .authority("api.dandelion.eu")
                            .appendPath("datatxt").appendPath("sim").appendPath("v1")
                            .appendQueryParameter("text1", input)
                            .appendQueryParameter("text2", phrases[i])
                            .appendQueryParameter("token", credentials);
                    String myUrl = builder.build().toString();
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.GET, myUrl, (String) null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    //mTxtDisplay.setText("Response: " + response.toString());
                                    //messages.add(response.toString());
                                    //itemsAdapter.notifyDataSetChanged();
                                    //listOfMessage.setSelection(itemsAdapter.getCount()-1);

                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO Auto-generated method stub

                                }
                            });
                    Volley.newRequestQueue(getApplicationContext().getApplicationContext()).add(jsObjRequest);
                }
                */
//                String res = cat.getResponse(input);
//                messages.add(res);
            }
        });

        //Check if not sign-in then navigate Signin page
        /*
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(activity_main,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
            //Load content
            displayChatMessage();
        }
        */
        displayChatMessage();
    }



    private void displayChatMessage() {

        listOfMessage = (ListView)findViewById(R.id.list_of_message);
        itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, messages);
        listOfMessage.setAdapter(itemsAdapter);
        /*
        adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.list_item,FirebaseDatabase.getInstance().getReference())
        {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                    //Get references to the views of list_item.xml
                    TextView messageText, messageUser, messageTime;
                    messageText = (EmojiconTextView) v.findViewById(R.id.message_text);
                    messageUser = (TextView) v.findViewById(R.id.message_user);
                    messageTime = (TextView) v.findViewById(R.id.message_time);

                    messageText.setText(model.getMessageText());
                    messageUser.setText(model.getMessageUser());
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));

            }
        };
        listOfMessage.setAdapter(adapter);
        */

    }
}
