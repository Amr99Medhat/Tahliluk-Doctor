package com.abdelrahman.amr.tahliluk_doctor.firebase

import android.content.Context
import android.widget.Toast
import com.abdelrahman.amr.tahliluk_doctor.utilities.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class FirestoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()
    private lateinit var mDocumentReference: DocumentReference
    private val mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


    fun checkIfExist(
        collectionName: String,
        KeyNumber: String,
        inputNumber: String
    ): Task<QuerySnapshot> {
        val qS = mFireStore.collection(collectionName)
        val query = qS.whereEqualTo(KeyNumber, inputNumber)
        return query.get()
    }

    fun signInByCredentials(
        credential: PhoneAuthCredential
    ): Task<AuthResult> {
        return mFirebaseAuth.signInWithCredential(credential)
    }

    fun signUp(
        collectionName: String,
        lab: HashMap<Any, Any>,
    ): Task<DocumentReference> {
        val cR = mFireStore.collection(collectionName)
        return cR.add(lab)
    }

    fun signIn(
        collectionName: String,
        KeyNumber: String,
        inputNumber: String,
        KeyPassword: String,
        inputPassword: String
    ): Task<QuerySnapshot> {
        val qs = mFireStore.collection(collectionName)
        val query = qs.whereEqualTo(KeyNumber, inputNumber)
            .whereEqualTo(KeyPassword, inputPassword)
        return query.get()
    }

    fun getLabs(collectionName: String): Task<QuerySnapshot> {
        val qs = mFireStore.collection(collectionName)
        return qs.get()
    }


    fun getReservations(collectionName: String,
                            labId:String,
                            orderDate:String,
                            orderState:String
                           ):
            Task<QuerySnapshot> {
        val qs = mFireStore.collection(collectionName).whereEqualTo(Constants.KEY_DOCTOR_LAB_ID,labId)
            .whereEqualTo(Constants.KEY_ORDER_DATE,orderDate)
            .whereEqualTo(Constants.ORDER_STATE,orderState)
        return qs.get()
    }

    fun getPatientInfo(collectionName: String, patientId: String):Task<DocumentSnapshot> {
       val qs =  mFireStore.collection(collectionName)
            .document(patientId)
            return qs.get()

    }

    fun updateReservation(collectionName: String,reservationId:String,reserveMap:HashMap<String,Any>){
      mFireStore.collection(collectionName)
            .document(reservationId)
            .update(reserveMap)

    }

}