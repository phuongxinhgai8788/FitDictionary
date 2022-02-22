package vn.edu.hanu.fitdictionary.helper;

import androidx.fragment.app.Fragment;

import vn.edu.hanu.fitdictionary.data.User;
import vn.edu.hanu.fitdictionary.data.Users;

public interface RenderFragment {
    public void openFragment(Fragment fragment, Boolean addToBackStack);
    public void updateUsers();
    public void setUser(User user);
    public Users getUsers();
}
