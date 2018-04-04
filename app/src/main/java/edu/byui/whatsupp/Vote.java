package edu.byui.whatsupp;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Vote</h1>
 * Keeps track of what the options are, how many votes they have
 * when the vote should close, which group it is assigned to,
 * and the creator
 *
 * @author  Dallin Wrathall
 * @version 1.0
 * @since   2018-03-21
 */

public class Vote {
    private String groupTitle;
    private int numOfGroupMembers;
    private String option1;
    private String option2;
    private String option3;
    private String option1Desc;
    private String option2Desc;
    private String option3Desc;
    private int votesFor1;
    private int votesFor2;
    private int votesFor3;
    private ArrayList<String> usersVoted;
    private String creator;
    private String closeDate;
    private String closeTime;

    public Vote(String g, int num, ThingToDo o1, ThingToDo o2, ThingToDo o3, String uid, String d, String t) {
        groupTitle = g;
        numOfGroupMembers = num;
        option1 = o1.getTitle();
        option1Desc = o1.getDescription();
        option2 = o2.getTitle();
        option2Desc = o2.getDescription();
        if (!(o3.getUrl().equals("null"))) {
            option3 = o3.getTitle();
            option3Desc = o3.getDescription();
        }
        creator = uid;
        votesFor1 = 0;
        votesFor2 = 0;
        votesFor3 = 0;

        closeDate = d;
        closeTime = t;

    }
    public Vote(String g, int num, String o1, String o1D, String o2,String o2D, String uid, String d, String t) {
        groupTitle = g;
        numOfGroupMembers = num;
        option1 = o1;
        option1Desc = o1D;
        option2 = o2;
        option2Desc = o2D;

        creator = uid;
        votesFor1 = 0;
        votesFor2 = 0;
        votesFor3 = 0;

        closeDate = d;
        closeTime = t;

    }

    public boolean closeVote(){
        // If all the members have voted
        if(numOfGroupMembers == usersVoted.size())
            return true;
        // If the date + time has been reached
        // return true
        return false;
    }

    public String getGroupRef() {
        return groupTitle;
    }

    public void setGroupRef(String group) {
        this.groupTitle = group;
    }

    public int getNumOfGroupMembers() {
        return numOfGroupMembers;
    }

    public void setNumOfGroupMembers(int numOfGroupMembers) {
        this.numOfGroupMembers = numOfGroupMembers;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption1Desc() {
        return option1Desc;
    }

    public void setOption1Desc(String option1Desc) {
        this.option1Desc = option1Desc;
    }

    public String getOption2Desc() {
        return option2Desc;
    }

    public void setOption2Desc(String option2Desc) {
        this.option2Desc = option2Desc;
    }

    public String getOption3Desc() {
        return option3Desc;
    }

    public void setOption3Desc(String option3Desc) {
        this.option3Desc = option3Desc;
    }

    public int getVotesFor1() {
        return votesFor1;
    }

    public void addVoteFor1() {
        votesFor1++;
    }

    public void setVotesFor1(int votesFor1) {
        this.votesFor1 = votesFor1;
    }

    public int getVotesFor2() {
        return votesFor2;
    }

    public void addVoteFor2() {
        votesFor2++;
    }

    public void setVotesFor2(int votesFor2) {
        this.votesFor2 = votesFor2;
    }

    public int getVotesFor3() {
        return votesFor3;
    }

    public void addVoteFor3() {
        votesFor3++;
    }

    public void setVotesFor3(int votesFor3) {
        this.votesFor3 = votesFor3;
    }

    public ArrayList<String> getUsersVoted() {
        return usersVoted;
    }

    public void setUsersVoted(ArrayList<String> usersVoted) {
        this.usersVoted = usersVoted;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }
}
