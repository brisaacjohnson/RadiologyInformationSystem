package Model;

import Controller.databaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class Patient {

      ////////////////////////
     //Variable Declaration//
    ////////////////////////
    private String  firstname;
    private String lastname;
    private String sex;
    private String email;
    private String address;
    private String phoneNumber;
    private String fullName;

    private String patientStatus;
    private LocalDate dob;

    private float balance;
    private int patientID, insuranceNumber;
    private int policyNumber;
    private ArrayList<Appointment> AppointmentList;


      ////////////////////
     //Database Queries//
    ////////////////////
    public static ResultSet queryAllPatients() throws Exception{
    ResultSet resultSet = databaseConnector.getConnection().prepareStatement(
    "select * FROM patient"
    ) .executeQuery();

    return resultSet;
    }

    public static ResultSet queryPatientInfo(int patientID) throws Exception{
        return databaseConnector.getConnection().prepareStatement(
                "SELECT * FROM patient " +
                        "WHERE patient_id = " + patientID).executeQuery();
    }

    public static ResultSet queryPatientAppointments(int patientId) throws Exception{
        return databaseConnector.getConnection().prepareStatement(
                "SELECT appointments.appointment_id, appointments.appointment_date, appointments.patient_sign_in_time, appointments.patient_sign_out_time, appointments.patient_status " +
                        "FROM appointments " +
                        "WHERE appointments.patient_id = " + patientId
        ).executeQuery();
    }

    public static ResultSet queryInfoForBillingList() throws Exception{
        return databaseConnector.getConnection().prepareStatement(
                "SELECT patient.patient_id, patient.first_name, patient.last_name, patient.status, patient.balance, address.street_name, address.city, address.state, address.zip FROM `patient` LEFT JOIN `address` ON patient.address_id = address.address_id"
        ).executeQuery();
    }

    public static int insertNewPatient(Patient patientToInsert, String address, String city, String state, String zip) throws Exception {
        Connection conn = databaseConnector.getConnection();
        int patientID = -1;
        if (insertAddress(address, city, state, zip) == 1) {

            ResultSet addressSet = queryAddress(address, city, state, zip);
            addressSet.next();
            int address_id = addressSet.getInt("address_id");

            PreparedStatement insertNewUser = conn.prepareStatement(
                    "INSERT INTO patient(patient_id, first_name, last_name, date_of_birth, sex, home_phone, email, insurance_number, policy_number, address_id, status, patient_medications_list)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, null)"
            );

            patientID = Math.abs((new Random()).nextInt(50000));

            insertNewUser.setInt(1, patientID);
            insertNewUser.setString(2, patientToInsert.getFirstname());
            insertNewUser.setString(3, patientToInsert.getLastname());
            insertNewUser.setString(4, patientToInsert.getDob().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            insertNewUser.setString(5, patientToInsert.getSex());
            insertNewUser.setString(6, String.valueOf(patientToInsert.getPhoneNumber()));
            insertNewUser.setString(7, patientToInsert.getEmail());
            insertNewUser.setString(8, String.valueOf(patientToInsert.getInsuranceNumber()));
            insertNewUser.setString(9, String.valueOf(patientToInsert.getPolicyNumber()));
            insertNewUser.setInt(10, address_id);
            insertNewUser.setString(11, "New Patient");
            insertNewUser.executeUpdate();
        }
        return patientID;
    }

    private static int insertAddress(String address, String city, String state, String zip) throws Exception{
        PreparedStatement insertAddress = databaseConnector.getConnection().prepareStatement(
                "INSERT INTO address(street_name, city, state, zip)" +
                        "VALUES(?, ?, ?, ?)");
        return prepareAddress(insertAddress, address, city, state, zip).executeUpdate();
    }

    private static ResultSet queryAddress(String address, String city, String state, String zip) throws Exception{
        PreparedStatement addressQuery = databaseConnector.getConnection().prepareStatement(
                "SELECT address_id FROM address " +
                        "WHERE street_name = ? AND city = ? AND state = ? AND zip = ?");
        return prepareAddress(addressQuery, address, city, state, zip).executeQuery();
    }
    public static ResultSet queryAddress(int addressID) throws Exception{
        return databaseConnector.getConnection().prepareStatement(
                        "SELECT * FROM `address` WHERE `address_id` = " + addressID
        ).executeQuery();
    }

    private static PreparedStatement prepareAddress(PreparedStatement statement, String address, String city, String state, String zip) throws Exception{
        statement.setString(1, address);
        statement.setString(2, city);
        statement.setString(3, state);
        statement.setInt(4, Integer.parseInt(zip));
        return statement;
    }



      ///////////////////
     //Getters/Setters//
    ///////////////////
    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getDob() {
        return dob;
    }
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) { this.patientID = patientID; }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public int getInsuranceNumber(){return insuranceNumber;}
    public void setInsuranceNumber(int insuranceNumber){this.insuranceNumber = insuranceNumber;}

    public int getPolicyNumber() {
        return this.policyNumber;
    }
    public void setPolicyNumber(int policyNumber) {
        this.policyNumber = policyNumber;
    }

    public ArrayList<Appointment> getAppointmentList() {
        return AppointmentList;
    }
    public void setAppointmentList(ArrayList<Appointment> appointmentList) {
        AppointmentList = appointmentList;
    }

    public String getPatientFullName() {
        return fullName;
    }
    public void setPatientFullName(String patientFullName) {
        this.fullName = patientFullName;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPatientStatus() { return patientStatus; }
    public void setPatientStatus(String patientStatus) { this.patientStatus = patientStatus; }

    public float getBalance() { return balance; }
    public void setBalance(float balance) { this.balance = balance; }





    ////////////////
     //Constructors//
    ////////////////
    public Patient(){
        this.patientID          = -1;
        this.firstname          = null;
        this.lastname           = null;
        this.dob                = null;
        this.sex                = null;
        this.phoneNumber        = null;
        this.email              = null;
        this.address            = null;
        this.insuranceNumber    = -1;
        this.policyNumber       = -1;
        this.AppointmentList    = new ArrayList<>();
    }

    public Patient(int patientID, String fullName, String billingAddress, String status, float balance){
        this.patientID = patientID;
        this.fullName = fullName;
        this.address = billingAddress;
        this.patientStatus = status;
        this.balance = balance;
    }

    public Patient(int patientID, String firstname, String lastname, String phoneNumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.patientID = patientID;
    }

    public Patient(int PatientID, String firstname, String lastname, LocalDate DoB, String Sex, String phoneNumber, String email, int insuranceNumber, int policyNumber, String address ){
        this.patientID          = PatientID;
        this.firstname          = firstname;
        this.lastname           = lastname;
        this.dob                = DoB;
        this.sex                = Sex;
        this.phoneNumber        = phoneNumber;
        this.email              = email;
        this.insuranceNumber    = insuranceNumber;
        this.policyNumber       = policyNumber;
        this.address            = address;
        this.AppointmentList    = new ArrayList<>();
    }

    public Patient(String firstname, String lastname, String sex, String email, LocalDate dob, String phoneNumber, int insuranceNumber, int policyNumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.sex = sex;
        this.email = email;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.insuranceNumber = insuranceNumber;
        this.policyNumber = policyNumber;
    }
}
