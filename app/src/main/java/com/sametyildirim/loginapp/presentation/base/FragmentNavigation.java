package com.sametyildirim.loginapp.presentation.base;


public interface FragmentNavigation {
    interface View {
        void attachPresenter(Presenter presenter);
    }

    interface Presenter {
        void addFragment(BaseFragment fragment);
    }
}
