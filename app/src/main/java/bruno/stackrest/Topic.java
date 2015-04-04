package bruno.stackrest;

/**
 * Created by Bruno on 2015-04-03.
 */
public class Topic {

    private String display_name, user_image, answer_count, title, link;


    public String getdisplay_name ()   {  return display_name;  }
    public String getuser_image ()   {  return user_image;  }
    public String getanswer_count ()   {  return answer_count;  }
    public String gettitle ()          {  return title;  }
    public String getlink ()           {  return link;  }

    public Topic(String display_name, String user_image, String answer_count, String title, String link)  { //This replaces the void method and is used to create new Topic objects
        this.display_name= display_name;
        this.user_image= user_image;
        this.answer_count = answer_count ;
        this.title = title ;
        this.link = link ;
    }




}
