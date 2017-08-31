package xFastRpc.sample.api;

/**
 * @author 徐浩然
 * @version Myname, 2017-08-31
 */
public class MyName
{
    private String firstName;
    private String lastName;

    public MyName(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
}
