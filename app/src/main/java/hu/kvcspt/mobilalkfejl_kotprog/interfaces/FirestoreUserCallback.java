package hu.kvcspt.mobilalkfejl_kotprog.interfaces;

import hu.kvcspt.mobilalkfejl_kotprog.models.User;

public interface FirestoreUserCallback {
    void onUserFetched(User user);
}
