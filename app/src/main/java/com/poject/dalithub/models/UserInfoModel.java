package com.poject.dalithub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 1/6/2016.
 */
public class UserInfoModel extends DalitHubBaseModel {
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("Mobile")
    @Expose
    private String Mobile;
    @SerializedName("emailId")
    @Expose
    private String emailId;
    @SerializedName("Address")
    @Expose
    private String Address;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("companyWebsite")
    @Expose
    private String companyWebsite;
    @SerializedName("BioGraphy")
    @Expose
    private String BioGraphy;
    @SerializedName("Skills")
    @Expose
    private String Skills;
    @SerializedName("profileImage")
    @Expose
    private String profileImage;
    @SerializedName("showEmail")
    @Expose
    private boolean showEmail;
    @SerializedName("showMobile")
    @Expose
    private boolean showMobile;
    @SerializedName("showCompanyDetails")
    @Expose
    private boolean showCompanyDetails;


    /**
     * @return The firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname The firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return The lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname The lastname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return The Mobile
     */
    public String getMobile() {
        return Mobile;
    }

    /**
     * @param Mobile The Mobile
     */
    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    /**
     * @return The emailId
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     * @param emailId The emailId
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    /**
     * @return The Address
     */
    public String getAddress() {
        return Address;
    }

    /**
     * @param Address The Address
     */
    public void setAddress(String Address) {
        this.Address = Address;
    }

    /**
     * @return The company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company The company
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return The companyWebsite
     */
    public String getCompanyWebsite() {
        return companyWebsite;
    }

    /**
     * @param companyWebsite The companyWebsite
     */
    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    /**
     * @return The BioGraphy
     */
    public String getBioGraphy() {
        return BioGraphy;
    }

    /**
     * @param BioGraphy The BioGraphy
     */
    public void setBioGraphy(String BioGraphy) {
        this.BioGraphy = BioGraphy;
    }

    /**
     * @return The Skills
     */
    public String getSkills() {
        return Skills;
    }

    /**
     * @param Skills The Skills
     */
    public void setSkills(String Skills) {
        this.Skills = Skills;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isShowEmail() {
        return showEmail;
    }

    public void setShowEmail(boolean showEmail) {
        this.showEmail = showEmail;
    }

    public boolean isShowCompanyDetails() {
        return showCompanyDetails;
    }

    public void setShowCompanyDetails(boolean showCompanyDetails) {
        this.showCompanyDetails = showCompanyDetails;
    }

    public boolean isShowMobile() {
        return showMobile;
    }

    public void setShowMobile(boolean showMobile) {
        this.showMobile = showMobile;
    }
}
