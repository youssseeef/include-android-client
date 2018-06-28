package com.includecar.includecar.fragments.userdisplaydetails;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.includecar.includecar.R;
import com.includecar.includecar.fragments.user.UserFragment;
import com.includecar.includecar.network.login.MedicalDataFetcher;
import com.includecar.includecar.network.login.MedicalDataUpdater;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import io.paperdb.Paper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DisplayUserDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DisplayUserDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayUserDetailsFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<String> medicalUsersAvailable;
    private ArrayList<JSONObject> medicalDataForUsers;

    private Button mButtonPrevious;
    private Button mButtonNext;
    private int currentPosition = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView mFullNameEditText;
    private TextView mEmail;
    private TextView mPhoneNumber;
    private TextView mEmergencyContactName;
    private TextView mEmergencyContactNumber;
    private TextView mEditTextAgeDatePicker;
    private TextView mSurgicalHistory;
    private TextView mMedications;
    private TextView mAllergiesToDrugs;
    private TextView mBloodGroup;
    private RadioGroup mAdditcion;
    private RadioGroup mSmoker;
    private RadioGroup mCancer;
    private RadioGroup mChronicObstructivePulmonary;
    private RadioGroup mDisease;
    private RadioGroup mClottingDisorder;
    private RadioGroup mHeartFailure;
    private RadioGroup mDiabetes;
    private RadioGroup mEmhysema;
    private RadioGroup mHepatitis;
    private RadioGroup mHypertension;
    private RadioGroup mMyocardialInfraction;
    private RadioGroup mSeizures;
    private RadioGroup mStrokes;

    private OnFragmentInteractionListener mListener;

    public DisplayUserDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisplayUserDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DisplayUserDetailsFragment newInstance(String param1, String param2) {
        DisplayUserDetailsFragment fragment = new DisplayUserDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_display_user_details, container, false);
        mFullNameEditText = view.findViewById(R.id.nameInput);
        mButtonPrevious = view.findViewById(R.id.previous_button);
        mButtonNext = view.findViewById(R.id.next_button);
        mEmail = view.findViewById(R.id.emailInput);
        mPhoneNumber = view.findViewById(R.id.phoneInput);
        mEmergencyContactName = view.findViewById(R.id.emergencyContactName);
        mEmergencyContactNumber = view.findViewById(R.id.emergencyContactNumber);
        mEditTextAgeDatePicker = view.findViewById(R.id.ageInput);
        mSurgicalHistory = view.findViewById(R.id.surgicalHistoryInput);
        mMedications = view.findViewById(R.id.medicationsInput);
        mAllergiesToDrugs = view.findViewById(R.id.allergiesToDrugsInput);
        mBloodGroup = view.findViewById(R.id.bloodGroup);
        mAdditcion = view.findViewById(R.id.addictRadio);
        mSmoker = view.findViewById(R.id.smokerRadio);
        mCancer = view.findViewById(R.id.cancerRadio);
        mDisease = view.findViewById(R.id.diseaseRadio);
        mChronicObstructivePulmonary = view.findViewById(R.id.chronicObstructivePulmonaryRadio);
        mClottingDisorder = view.findViewById(R.id.clottingDisorderRadio);
        mHeartFailure = view.findViewById(R.id.heartFailureRadio);
        mEmhysema = view.findViewById(R.id.emhysemaRadio);
        mDiabetes = view.findViewById(R.id.diabetesRadio);
        mHepatitis = view.findViewById(R.id.hepatitisRadio);
        mHypertension = view.findViewById(R.id.hypertensionRadio);
        mMyocardialInfraction = view.findViewById(R.id.myocardialInfractionRadio);
        mSeizures = view.findViewById(R.id.seizuresRadio);
        mStrokes = view.findViewById(R.id.strokesRadio);
        final MedicalDataFetcher mdf = new MedicalDataFetcher();
        String carId = Paper.book().read("car_Id");
        //find a way to start this when the carId Changes means another car.
        mButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition--;
                manageThePreviousNextButtons();
                updateTheScreenWithCurrentPosition();


            }
        });
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition++;
                manageThePreviousNextButtons();
                updateTheScreenWithCurrentPosition();
            }
        });
        mdf.fetchAllUsers(carId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ERROR_CALL", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();
                if(response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(bodyString);
                        ArrayList<String> arrayOfAssociatedUsers = new ArrayList<>();
                        medicalDataForUsers = new ArrayList<>();

                        Iterator<String> it = (Iterator<String>) jsonObject.keys();
                        while(it.hasNext()){
                            arrayOfAssociatedUsers.add(it.next());
                        }
                        medicalUsersAvailable = arrayOfAssociatedUsers;
                        for (final String medicalUser:
                                medicalUsersAvailable) {
                            mdf.fetchUserDataReloaded(medicalUser, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    backgroundThreadShortToast(DisplayUserDetailsFragment.this.getActivity(),"Some error happened fetching the data..");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    ResponseBody responseBody = response.body();
                                    String bodyString = responseBody.string();
                                    //backgroundThreadShortToast(UserFragment.this.getActivity(),bodyString);
                                    Log.e("LOGIN_ACTIVITY",bodyString);
                                    if(response.code() == 200){
                                        try {
                                            JSONObject jsonObject = new JSONObject( bodyString);
                                            JSONObject answer2 = (JSONObject)jsonObject.get("answer");
                                            medicalDataForUsers.add(answer2);
                                            Log.d("ANSWERR" , answer2.toString());
                                            if(medicalDataForUsers.size() == medicalUsersAvailable.size()){
                                                currentPosition = 0;

                                                JSONObject answer = medicalDataForUsers.get(0);
                                                final String fullNameAnswer = answer.get("fullName").toString();
                                                final String emailAnswer = answer.get("username").toString();
                                                final String phoneNumberAnswer = answer.get("phoneNumber").toString();
                                                final String emergencyContactNameAnswer = answer.get("emergencyContactName").toString();
                                                final String emergencyContactNumberAnswer = answer.get("emergencyPhoneNumber").toString();
                                                final String dateDay = answer.get("birthDay").toString();
                                                final String dateMonth = answer.get("birthMonth").toString();
                                                final String dateYear = answer.get("birthYear").toString();
                                                final String dateFormatted = dateMonth+"/"+dateDay+"/"+dateYear;
                                                final String surgicalHistoryAnswer = answer.get("surgicalHistory").toString();
                                                final String currentMedicationsAnswer = answer.get("currentMedications").toString();
                                                final String allergiesToDrugsAnswer = answer.get("allergiesToDrugs").toString();
                                                final String bloodGroupAnswer = answer.get("bloodGroup").toString();
                                                final String addictAnswer = answer.get("addict").toString();
                                                final String smokerAnswer = answer.get("smoker").toString();
                                                final String cancerAnswer = answer.get("cancer").toString();
                                                final String chronicObstructiveAnswer = answer.get("chronicObstructive").toString();
                                                final String diseaseAnswer = answer.get("disease").toString();
                                                final String clotticDisorderAnswer = answer.get("clotticDisorder").toString();
                                                final String heartFailureAnswer = answer.get("heartFailure").toString();
                                                final String diabetesAnswer = answer.get("diabetes").toString();
                                                final String emhysemaAnswer = answer.get("emhysema").toString();
                                                final String hepatitisAnswer = answer.get("hepatitis").toString();
                                                final String hyperTensionAnswer = answer.get("hyperTension").toString();
                                                final String myocardialInfractionAnswer = answer.get("myocardialInfraction").toString();
                                                final String seizuresAnswer = answer.get("seizures").toString();
                                                final String strokesAnswer = answer.get("strokes").toString();
                                                DisplayUserDetailsFragment.this.getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //update UI with pre-existing data from the server
                                                        manageThePreviousNextButtons();
                                                        mFullNameEditText.setText(fullNameAnswer);
                                                        mEmail.setText(emailAnswer);
                                                        mPhoneNumber.setText(phoneNumberAnswer);
                                                        mEmergencyContactName.setText(emergencyContactNameAnswer);
                                                        mEmergencyContactNumber.setText(emergencyContactNumberAnswer);
                                                        mEditTextAgeDatePicker.setText(dateFormatted);
                                                        mSurgicalHistory.setText(surgicalHistoryAnswer);
                                                        mMedications.setText(currentMedicationsAnswer);
                                                        mAllergiesToDrugs.setText(allergiesToDrugsAnswer);
                                                        mBloodGroup.setText("Blood Group: "+bloodGroupAnswer);
                                                        ((RadioButton)mAdditcion.getChildAt(getIndexForRadioGroup(addictAnswer))).setChecked(true);
                                                        ((RadioButton)mSmoker.getChildAt(getIndexForRadioGroup(smokerAnswer))).setChecked(true);
                                                        ((RadioButton)mCancer.getChildAt(getIndexForRadioGroup(cancerAnswer))).setChecked(true);
                                                        ((RadioButton)mChronicObstructivePulmonary.getChildAt(getIndexForRadioGroup(chronicObstructiveAnswer))).setChecked(true);
                                                        ((RadioButton)mClottingDisorder.getChildAt(getIndexForRadioGroup(clotticDisorderAnswer))).setChecked(true);
                                                        ((RadioButton)mDisease.getChildAt(getIndexForRadioGroup(diseaseAnswer))).setChecked(true);
                                                        ((RadioButton)mHeartFailure.getChildAt(getIndexForRadioGroup(heartFailureAnswer))).setChecked(true);
                                                        ((RadioButton)mDiabetes.getChildAt(getIndexForRadioGroup(diabetesAnswer))).setChecked(true);
                                                        ((RadioButton)mEmhysema.getChildAt(getIndexForRadioGroup(emhysemaAnswer))).setChecked(true);
                                                        ((RadioButton)mHepatitis.getChildAt(getIndexForRadioGroup(hepatitisAnswer))).setChecked(true);
                                                        ((RadioButton)mHypertension.getChildAt(getIndexForRadioGroup(hyperTensionAnswer))).setChecked(true);
                                                        ((RadioButton)mMyocardialInfraction.getChildAt(getIndexForRadioGroup(myocardialInfractionAnswer))).setChecked(true);
                                                        ((RadioButton)mSeizures.getChildAt(getIndexForRadioGroup(seizuresAnswer))).setChecked(true);
                                                        ((RadioButton)mStrokes.getChildAt(getIndexForRadioGroup(strokesAnswer))).setChecked(true);
                                                    }
                                                });
                                               // updateTheScreenWithCurrentPosition();

                                            }
                                        }catch(JSONException e){
                                            Log.d("JSON EXCEPTION BOY", e.toString());
                                        }
                                    }
                                    response.close();

                                }
                            });

                        }
                        //after everyone has been added
                        //display the first, set to 0,
                        //also setup buttons in the view section

                    }catch(JSONException je){
                        Log.e("JSON EE", je.toString());

                    }
                }
                response.close();
            }
        });
        /*
        mdf.fetchUserDataReloaded("youssefhanii@gmail.com", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                backgroundThreadShortToast(DisplayUserDetailsFragment.this.getActivity(),"Some error happened fetching the data..");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                String bodyString = responseBody.string();
                //backgroundThreadShortToast(UserFragment.this.getActivity(),bodyString);
                Log.e("LOGIN_ACTIVITY",bodyString);
                if(response.code() == 200){
                    try {
                        JSONObject jsonObject = new JSONObject( bodyString);
                        JSONObject answer = (JSONObject)jsonObject.get("answer");
                        final String fullNameAnswer = answer.get("fullName").toString();
                        final String phoneNumberAnswer = answer.get("phoneNumber").toString();
                        final String emergencyContactNameAnswer = answer.get("emergencyContactName").toString();
                        final String emergencyContactNumberAnswer = answer.get("emergencyPhoneNumber").toString();
                        final String dateDay = answer.get("birthDay").toString();
                        final String dateMonth = answer.get("birthMonth").toString();
                        final String dateYear = answer.get("birthYear").toString();
                        final String dateFormatted = dateMonth+"/"+dateDay+"/"+dateYear;
                        final String surgicalHistoryAnswer = answer.get("surgicalHistory").toString();
                        final String currentMedicationsAnswer = answer.get("currentMedications").toString();
                        final String allergiesToDrugsAnswer = answer.get("allergiesToDrugs").toString();
                        final String bloodGroupAnswer = answer.get("bloodGroup").toString();
                        final String addictAnswer = answer.get("addict").toString();
                        final String smokerAnswer = answer.get("smoker").toString();
                        final String cancerAnswer = answer.get("cancer").toString();
                        final String chronicObstructiveAnswer = answer.get("chronicObstructive").toString();
                        final String diseaseAnswer = answer.get("disease").toString();
                        final String clotticDisorderAnswer = answer.get("clotticDisorder").toString();
                        final String heartFailureAnswer = answer.get("heartFailure").toString();
                        final String diabetesAnswer = answer.get("diabetes").toString();
                        final String emhysemaAnswer = answer.get("emhysema").toString();
                        final String hepatitisAnswer = answer.get("hepatitis").toString();
                        final String hyperTensionAnswer = answer.get("hyperTension").toString();
                        final String myocardialInfractionAnswer = answer.get("myocardialInfraction").toString();
                        final String seizuresAnswer = answer.get("seizures").toString();
                        final String strokesAnswer = answer.get("strokes").toString();
                        DisplayUserDetailsFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //update UI with pre-existing data from the server
                                mFullNameEditText.setText(fullNameAnswer);
                                mPhoneNumber.setText(phoneNumberAnswer);
                                mEmergencyContactName.setText(emergencyContactNameAnswer);
                                mEmergencyContactNumber.setText(emergencyContactNumberAnswer);
                                mEditTextAgeDatePicker.setText(dateFormatted);
                                mSurgicalHistory.setText(surgicalHistoryAnswer);
                                mMedications.setText(currentMedicationsAnswer);
                                mAllergiesToDrugs.setText(allergiesToDrugsAnswer);
                                mBloodGroup.setText("Blood Group: "+bloodGroupAnswer);
                                ((RadioButton)mAdditcion.getChildAt(getIndexForRadioGroup(addictAnswer))).setChecked(true);
                                ((RadioButton)mSmoker.getChildAt(getIndexForRadioGroup(smokerAnswer))).setChecked(true);
                                ((RadioButton)mCancer.getChildAt(getIndexForRadioGroup(cancerAnswer))).setChecked(true);
                                ((RadioButton)mChronicObstructivePulmonary.getChildAt(getIndexForRadioGroup(chronicObstructiveAnswer))).setChecked(true);
                                ((RadioButton)mClottingDisorder.getChildAt(getIndexForRadioGroup(clotticDisorderAnswer))).setChecked(true);
                                ((RadioButton)mDisease.getChildAt(getIndexForRadioGroup(diseaseAnswer))).setChecked(true);
                                ((RadioButton)mHeartFailure.getChildAt(getIndexForRadioGroup(heartFailureAnswer))).setChecked(true);
                                ((RadioButton)mDiabetes.getChildAt(getIndexForRadioGroup(diabetesAnswer))).setChecked(true);
                                ((RadioButton)mEmhysema.getChildAt(getIndexForRadioGroup(emhysemaAnswer))).setChecked(true);
                                ((RadioButton)mHepatitis.getChildAt(getIndexForRadioGroup(hepatitisAnswer))).setChecked(true);
                                ((RadioButton)mHypertension.getChildAt(getIndexForRadioGroup(hyperTensionAnswer))).setChecked(true);
                                ((RadioButton)mMyocardialInfraction.getChildAt(getIndexForRadioGroup(myocardialInfractionAnswer))).setChecked(true);
                                ((RadioButton)mSeizures.getChildAt(getIndexForRadioGroup(seizuresAnswer))).setChecked(true);
                                ((RadioButton)mStrokes.getChildAt(getIndexForRadioGroup(strokesAnswer))).setChecked(true);
                            }
                        });

                    }catch(JSONException e){
                        Log.d("JSON EXCEPTION BOY", e.toString());
                    }
                }

            }
        });
        */
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public static void backgroundThreadShortToast(final Context context,
                                                  final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private int getIndexForRadioGroup(String answer){
        Log.d("TAG TAG TAG"+answer, answer);
        if(answer.equals("Yes")){
            return 0;
        }else{
            return 1;
        }
    }


    private void updateTheScreenWithCurrentPosition(){
        Log.d("MED MED ALL", medicalUsersAvailable.toString());
        Log.d("MED MED ALL", String.valueOf(currentPosition));
        if(medicalDataForUsers.size() == medicalUsersAvailable.size() && currentPosition >= 0 && currentPosition <= medicalDataForUsers.size() -1){
            try {
                Log.d("POSPOS",String.valueOf(currentPosition));
                JSONObject answer = medicalDataForUsers.get(currentPosition);
                final String fullNameAnswer = answer.get("fullName").toString();
                final String emailAnswer = answer.get("username").toString();
                final String phoneNumberAnswer = answer.get("phoneNumber").toString();
                final String emergencyContactNameAnswer = answer.get("emergencyContactName").toString();
                final String emergencyContactNumberAnswer = answer.get("emergencyPhoneNumber").toString();
                final String dateDay = answer.get("birthDay").toString();
                final String dateMonth = answer.get("birthMonth").toString();
                final String dateYear = answer.get("birthYear").toString();
                final String dateFormatted = dateMonth + "/" + dateDay + "/" + dateYear;
                final String surgicalHistoryAnswer = answer.get("surgicalHistory").toString();
                final String currentMedicationsAnswer = answer.get("currentMedications").toString();
                final String allergiesToDrugsAnswer = answer.get("allergiesToDrugs").toString();
                final String bloodGroupAnswer = answer.get("bloodGroup").toString();
                final String addictAnswer = answer.get("addict").toString();
                final String smokerAnswer = answer.get("smoker").toString();
                final String cancerAnswer = answer.get("cancer").toString();
                final String chronicObstructiveAnswer = answer.get("chronicObstructive").toString();
                final String diseaseAnswer = answer.get("disease").toString();
                final String clotticDisorderAnswer = answer.get("clotticDisorder").toString();
                final String heartFailureAnswer = answer.get("heartFailure").toString();
                final String diabetesAnswer = answer.get("diabetes").toString();
                final String emhysemaAnswer = answer.get("emhysema").toString();
                final String hepatitisAnswer = answer.get("hepatitis").toString();
                final String hyperTensionAnswer = answer.get("hyperTension").toString();
                final String myocardialInfractionAnswer = answer.get("myocardialInfraction").toString();
                final String seizuresAnswer = answer.get("seizures").toString();
                final String strokesAnswer = answer.get("strokes").toString();
                DisplayUserDetailsFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //update UI with pre-existing data from the server
                        manageThePreviousNextButtons();
                        mFullNameEditText.setText(fullNameAnswer);
                        mEmail.setText(emailAnswer);
                        mPhoneNumber.setText(phoneNumberAnswer);
                        mEmergencyContactName.setText(emergencyContactNameAnswer);
                        mEmergencyContactNumber.setText(emergencyContactNumberAnswer);
                        mEditTextAgeDatePicker.setText(dateFormatted);
                        mSurgicalHistory.setText(surgicalHistoryAnswer);
                        mMedications.setText(currentMedicationsAnswer);
                        mAllergiesToDrugs.setText(allergiesToDrugsAnswer);
                        mBloodGroup.setText("Blood Group: " + bloodGroupAnswer);
                        ((RadioButton) mAdditcion.getChildAt(getIndexForRadioGroup(addictAnswer))).setChecked(true);
                        ((RadioButton) mSmoker.getChildAt(getIndexForRadioGroup(smokerAnswer))).setChecked(true);
                        ((RadioButton) mCancer.getChildAt(getIndexForRadioGroup(cancerAnswer))).setChecked(true);
                        ((RadioButton) mChronicObstructivePulmonary.getChildAt(getIndexForRadioGroup(chronicObstructiveAnswer))).setChecked(true);
                        ((RadioButton) mClottingDisorder.getChildAt(getIndexForRadioGroup(clotticDisorderAnswer))).setChecked(true);
                        ((RadioButton) mDisease.getChildAt(getIndexForRadioGroup(diseaseAnswer))).setChecked(true);
                        ((RadioButton) mHeartFailure.getChildAt(getIndexForRadioGroup(heartFailureAnswer))).setChecked(true);
                        ((RadioButton) mDiabetes.getChildAt(getIndexForRadioGroup(diabetesAnswer))).setChecked(true);
                        ((RadioButton) mEmhysema.getChildAt(getIndexForRadioGroup(emhysemaAnswer))).setChecked(true);
                        ((RadioButton) mHepatitis.getChildAt(getIndexForRadioGroup(hepatitisAnswer))).setChecked(true);
                        ((RadioButton) mHypertension.getChildAt(getIndexForRadioGroup(hyperTensionAnswer))).setChecked(true);
                        ((RadioButton) mMyocardialInfraction.getChildAt(getIndexForRadioGroup(myocardialInfractionAnswer))).setChecked(true);
                        ((RadioButton) mSeizures.getChildAt(getIndexForRadioGroup(seizuresAnswer))).setChecked(true);
                        ((RadioButton) mStrokes.getChildAt(getIndexForRadioGroup(strokesAnswer))).setChecked(true);
                    }
                });
            }catch(JSONException je){
                Log.e("JSON EX", je.toString());
            }

        }
    }

    private void manageThePreviousNextButtons(){
        if(currentPosition == (medicalDataForUsers.size() -1) ){
            mButtonPrevious.setEnabled(true);
            mButtonNext.setEnabled(false);
        }else if(currentPosition == 0){
            mButtonNext.setEnabled(true);
            mButtonPrevious.setEnabled(false);
        }else{
            mButtonPrevious.setEnabled(true);
            mButtonNext.setEnabled(true);
        }
    }

}

