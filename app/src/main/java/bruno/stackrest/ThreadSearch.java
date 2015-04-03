package bruno.stackrest;

/**
 * Created using http://pojo.sodhanalibrary.com/  based off the http://api.stackexchange.com/2.2/search?page=1&pagesize=3&order=desc&sort=activity&tagged=android&site=stackoverflow JSON results
 */
public class ThreadSearch {
    private String quota_max;

    private Items[] items;

    private String has_more;

    private String quota_remaining;

    public String getQuota_max ()
    {
        return quota_max;
    }

    public void setQuota_max (String quota_max)
    {
        this.quota_max = quota_max;
    }

    public Items[] getItems ()
    {
        return items;
    }

    public void setItems (Items[] items)
    {
        this.items = items;
    }

    public String getHas_more ()
    {
        return has_more;
    }

    public void setHas_more (String has_more)
    {
        this.has_more = has_more;
    }

    public String getQuota_remaining ()
    {
        return quota_remaining;
    }

    public void setQuota_remaining (String quota_remaining)
    {
        this.quota_remaining = quota_remaining;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [quota_max = "+quota_max+", items = "+items+", has_more = "+has_more+", quota_remaining = "+quota_remaining+"]";
    }



    public class Items
    {
        private String creation_date;

        private String[] tags;

        private String title;

        private String link;

        private String score;

        private String answer_count;

        private Owner owner;

        private String last_activity_date;

        private String question_id;

        private String view_count;

        private String is_answered;

        public String getCreation_date ()
        {
            return creation_date;
        }

        public void setCreation_date (String creation_date)
        {
            this.creation_date = creation_date;
        }

        public String[] getTags ()
        {
            return tags;
        }

        public void setTags (String[] tags)
        {
            this.tags = tags;
        }

        public String getTitle ()
        {
            return title;
        }

        public void setTitle (String title)
        {
            this.title = title;
        }

        public String getLink ()
        {
            return link;
        }

        public void setLink (String link)
        {
            this.link = link;
        }

        public String getScore ()
        {
            return score;
        }

        public void setScore (String score)
        {
            this.score = score;
        }

        public String getAnswer_count ()
        {
            return answer_count;
        }

        public void setAnswer_count (String answer_count)
        {
            this.answer_count = answer_count;
        }

        public Owner getOwner ()
        {
            return owner;
        }

        public void setOwner (Owner owner)
        {
            this.owner = owner;
        }

        public String getLast_activity_date ()
        {
            return last_activity_date;
        }

        public void setLast_activity_date (String last_activity_date)
        {
            this.last_activity_date = last_activity_date;
        }

        public String getQuestion_id ()
        {
            return question_id;
        }

        public void setQuestion_id (String question_id)
        {
            this.question_id = question_id;
        }

        public String getView_count ()
        {
            return view_count;
        }

        public void setView_count (String view_count)
        {
            this.view_count = view_count;
        }

        public String getIs_answered ()
        {
            return is_answered;
        }

        public void setIs_answered (String is_answered)
        {
            this.is_answered = is_answered;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [creation_date = "+creation_date+", tags = "+tags+", title = "+title+", link = "+link+", score = "+score+", answer_count = "+answer_count+", owner = "+owner+", last_activity_date = "+last_activity_date+", question_id = "+question_id+", view_count = "+view_count+", is_answered = "+is_answered+"]";
        }
    }



    public class Owner
    {
        private String display_name;

        private String user_type;

        private String profile_image;

        private String link;

        private String reputation;

        private String user_id;

        public String getDisplay_name ()
        {
            return display_name;
        }

        public void setDisplay_name (String display_name)
        {
            this.display_name = display_name;
        }

        public String getUser_type ()
        {
            return user_type;
        }

        public void setUser_type (String user_type)
        {
            this.user_type = user_type;
        }

        public String getProfile_image ()
        {
            return profile_image;
        }

        public void setProfile_image (String profile_image)
        {
            this.profile_image = profile_image;
        }

        public String getLink ()
        {
            return link;
        }

        public void setLink (String link)
        {
            this.link = link;
        }

        public String getReputation ()
        {
            return reputation;
        }

        public void setReputation (String reputation)
        {
            this.reputation = reputation;
        }

        public String getUser_id ()
        {
            return user_id;
        }

        public void setUser_id (String user_id)
        {
            this.user_id = user_id;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [display_name = "+display_name+", user_type = "+user_type+", profile_image = "+profile_image+", link = "+link+", reputation = "+reputation+", user_id = "+user_id+"]";
        }
    }


}
