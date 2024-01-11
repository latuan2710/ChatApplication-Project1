import java.util.Date;

public class User {
    private String _fisrtName;
    private String _lastName;
    private String _fullName;
    private Date _dateOfBirth;
    private boolean _isMale;
    private String _hashPassword;

    public User(String fisrtName, String lastName, String fullName, Date dateOfBirth, boolean isMale,
            String hashPassword) {
        this._fisrtName = fisrtName;
        this._lastName = lastName;
        this._fullName = fullName;
        this._dateOfBirth = dateOfBirth;
        this._isMale = isMale;
        this._hashPassword = hashPassword;
    }

    public String getFisrtName() {
        return _fisrtName;
    }

    public void setFisrtName(String fisrtName) {
        this._fisrtName = fisrtName;
    }

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        this._lastName = lastName;
    }

    public String getFullName() {
        return _fullName;
    }

    public void setFullName(String fullName) {
        this._fullName = fullName;
    }

    public Date getDateOfBirth() {
        return _dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this._dateOfBirth = dateOfBirth;
    }

    public boolean isMale() {
        return _isMale;
    }

    public void setIsMale(boolean isMale) {
        this._isMale = isMale;
    }

    public void setHashPassword(String hashPassword) {
        this._hashPassword = hashPassword;
    }

}