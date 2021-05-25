package io.library.dao;

import io.library.datasource.DataSourceJSON;
import io.library.menu.AdminMenu;
import io.library.menu.UserMenu;
import io.library.model.AccessLevel;
import io.library.model.User;
import org.json.JSONArray;
import org.json.JSONObject;


public class UserDaoJSON implements IUserDao {

    private int getUserCount() {
        JSONArray users = DataSourceJSON.getInstance().readUsers();
        return users.length();
    }

    @Override
    public boolean checkUserNameAvailability(String userName) {
        boolean result = true;
        JSONArray users = DataSourceJSON.getInstance().readUsers();
        for(var user : users) {
            JSONObject object = (JSONObject) user;
            if(object.getString("name").equals(userName)) {
                result = false;
                break;
            }
        }
        return result;
    }

    @Override
    public void addUser(User user) {
        JSONObject userObject = new JSONObject();
        int index = getUserCount() + 1;
        userObject.put("id", index);
        userObject.put("name", user.getUserName());
        userObject.put("password", user.getPassword());
        userObject.put("access-level", user.getAccessLevel().ordinal());
        userObject.put("mobile-number", user.getMobileNumber());
        userObject.put("age", user.getAge());
        DataSourceJSON.getInstance().writeUser(userObject);
    }

    @Override
    public int getUserId(String userName) {
        JSONArray users = DataSourceJSON.getInstance().readUsers();
        int userId = -1;
        for(var user : users) {
            JSONObject object = (JSONObject) user;
            String name = object.getString("name");
            if(name.equals(userName)) {
                userId = object.getInt("id");
                break;
            }
        }
        return userId;
    }

    @Override
    public User getUser(String userName) {
        JSONArray users = DataSourceJSON.getInstance().readUsers();
        User resultUser = null;
        for(var user : users) {
            JSONObject object = (JSONObject) user;
            String name = object.getString("name");
            if(name.equals(userName)) {
                String password = object.getString("password");
                AccessLevel accessLevel = AccessLevel.values()[object.getInt("access-level")];
                String mobileNumber = object.getString("mobile-number");
                int age = object.getInt("age");
                if(accessLevel == AccessLevel.ADMIN) {
                    resultUser = new User(name, password, mobileNumber, age, accessLevel, new AdminMenu());
                }
                else {
                    resultUser = new User(name, password, mobileNumber, age, accessLevel, new UserMenu());
                }
                break;
            }
        }
        return resultUser;
    }
}
