package com.includecar.includecar.fragments.user;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
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
import android.widget.Toast;

import com.includecar.includecar.R;
import com.includecar.includecar.activities.LoginActivity.LoginActivity;
import com.includecar.includecar.helperclasses.SelectDateFragment;
import com.includecar.includecar.network.login.MedicalDataFetcher;
import com.includecar.includecar.network.login.MedicalDataUpdater;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.paperdb.Paper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText mFullNameEditText;
    private EditText mEmail;
    private EditText mPhoneNumber;
    private EditText mEmergencyContactName;
    private EditText mEmergencyContactNumber;
    private EditText mEditTextAgeDatePicker;
    private EditText mSurgicalHistory;
    private EditText mMedications;
    private EditText mAllergiesToDrugs;
    private Spinner mBloodGroup;
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
    private Button mSubmitButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        final View view = inflater.inflate(R.layout.fragment_user, container, false);
        mEditTextAgeDatePicker = view.findViewById(R.id.ageInput);
        mEditTextAgeDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDateFragment newFragment = new SelectDateFragment();
                newFragment.setEditText(mEditTextAgeDatePicker);
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        mFullNameEditText = view.findViewById(R.id.nameInput);
        mEmail = view.findViewById(R.id.emailInput);
        String loggedUserEmail = Paper.book().read("login_username").toString();
        mEmail.setText(loggedUserEmail);
        mPhoneNumber = view.findViewById(R.id.phoneInput);
        mEmergencyContactName = view.findViewById(R.id.emergencyContactName);
        mEmergencyContactNumber = view.findViewById(R.id.emergencyContactNumber);
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
        mSubmitButton = view.findViewById(R.id.submitButton);
        MedicalDataFetcher mdf = new MedicalDataFetcher();
        mdf.fetchUserData(loggedUserEmail, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                backgroundThreadShortToast(UserFragment.this.getActivity(),"Some error happened fetching the data..");
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
                        UserFragment.this.getActivity().runOnUiThread(new Runnable() {
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
                                mBloodGroup.setSelection(getIndex(mBloodGroup,bloodGroupAnswer));
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
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strokesAnswer = ((RadioButton)view.findViewById(mStrokes.getCheckedRadioButtonId())).getText().toString();
                String seizuresAnswer = ((RadioButton)view.findViewById(mSeizures.getCheckedRadioButtonId())).getText().toString();
                String diseaseAnswer = ((RadioButton)view.findViewById(mDisease.getCheckedRadioButtonId())).getText().toString();
                String myocardialInfractionAnswer = ((RadioButton)view.findViewById(mMyocardialInfraction.getCheckedRadioButtonId())).getText().toString();
                String hyperTensionAnswer = ((RadioButton)view.findViewById(mHypertension.getCheckedRadioButtonId())).getText().toString();
                String hepatitisAnswer = ((RadioButton)view.findViewById(mHepatitis.getCheckedRadioButtonId())).getText().toString();
                String diabetesAnswer = ((RadioButton)view.findViewById(mDiabetes.getCheckedRadioButtonId())).getText().toString();
                String emhysemaAnswer = ((RadioButton)view.findViewById(mEmhysema.getCheckedRadioButtonId())).getText().toString();
                String heartFailureAnswer = ((RadioButton)view.findViewById(mHeartFailure.getCheckedRadioButtonId())).getText().toString();
                String clottingDiscorderAnswer = ((RadioButton)view.findViewById(mClottingDisorder.getCheckedRadioButtonId())).getText().toString();
                String chronicObstructivePulmonaryAnswer = ((RadioButton)view.findViewById(mChronicObstructivePulmonary.getCheckedRadioButtonId())).getText().toString();
                String cancerAnswer = ((RadioButton)view.findViewById(mCancer.getCheckedRadioButtonId())).getText().toString();
                String smokerAnswer = ((RadioButton)view.findViewById(mSmoker.getCheckedRadioButtonId())).getText().toString();
                String addictionAnswer = ((RadioButton)view.findViewById(mAdditcion.getCheckedRadioButtonId())).getText().toString();
                String bloodGroupAnswer = (String)mBloodGroup.getSelectedItem();
                String allergiesAnswer = mAllergiesToDrugs.getText().toString();
                String medicationsAnswer = mMedications.getText().toString();
                String surgicalHistoryAnswer=  mSurgicalHistory.getText().toString();
                String birthDateAnswer = mEditTextAgeDatePicker.getText().toString();
                String emergencyContactNumberAnswer = mEmergencyContactNumber.getText().toString();
                String emergencyContactNameAnswer = mEmergencyContactName.getText().toString();
                String phoneNumberAnswer = mPhoneNumber.getText().toString();
                String fullNameAnswer = mFullNameEditText.getText().toString();
                String emailAnswer = mEmail.getText().toString();
                MedicalDataUpdater dataUpdate = new MedicalDataUpdater();
                dataUpdate.updateUserData(fullNameAnswer, emailAnswer, phoneNumberAnswer,
                        emergencyContactNameAnswer, emergencyContactNumberAnswer, birthDateAnswer.split("/")[2],
                        birthDateAnswer.split("/")[0], birthDateAnswer.split("/")[1],
                        surgicalHistoryAnswer, medicationsAnswer, allergiesAnswer, bloodGroupAnswer,
                        addictionAnswer, smokerAnswer, cancerAnswer, chronicObstructivePulmonaryAnswer, diseaseAnswer,
                        clottingDiscorderAnswer, heartFailureAnswer, diabetesAnswer, emhysemaAnswer, hepatitisAnswer,
                        hyperTensionAnswer, myocardialInfractionAnswer, seizuresAnswer, strokesAnswer, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if(response.code() == 200){
                                    backgroundThreadShortToast(UserFragment.this.getActivity(),"Data Updated! Awesome!");
                                }else{
                                    backgroundThreadShortToast(UserFragment.this.getActivity(),"Data not updated :( Check your connection!");
                                }
                            }
                        }
                );
            }
        });

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
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }
    private int getIndexForRadioGroup(String answer){
        Log.d("TAG TAG TAG"+answer, answer);
        if(answer.equals("Yes")){
            return 0;
        }else{
            return 1;
        }
    }
}
