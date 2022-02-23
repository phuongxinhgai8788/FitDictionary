package vn.edu.hanu.fitdictionary.helper;

import androidx.fragment.app.Fragment;

import vn.edu.hanu.fitdictionary.data.User;

public interface RenderFragment {
    public void openFragment(Fragment fragment, Boolean addToBackStack);
    public void setUser(User user);
}
