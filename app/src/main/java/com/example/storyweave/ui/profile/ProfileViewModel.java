    package com.example.storyweave.ui.profile;

    import androidx.lifecycle.LiveData;
    import androidx.lifecycle.MutableLiveData;
    import androidx.lifecycle.ViewModel;

    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.firestore.DocumentSnapshot;
    import com.google.firebase.firestore.FirebaseFirestore;

    public class ProfileViewModel extends ViewModel {
        private final MutableLiveData<String> email = new MutableLiveData<>();
        private final MutableLiveData<String> username = new MutableLiveData<>();
        private final MutableLiveData<Integer> storiesCount = new MutableLiveData<>(0);
        private final MutableLiveData<Integer> fragmentsCount = new MutableLiveData<>(0);
        private final MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
        private final MutableLiveData<String> error = new MutableLiveData<>();

        private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        private final FirebaseFirestore db = FirebaseFirestore.getInstance();

        public ProfileViewModel() {
            loadUserProfile();
        }

        private void loadUserProfile() {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                error.setValue("User not logged in");
                loading.setValue(false);
                return;
            }

            email.setValue(currentUser.getEmail());

            db.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String uname = documentSnapshot.getString("email").split("@")[0];
                            username.setValue(uname != null ? uname : "Unknown");

                            db.collection("stories")
                                    .whereEqualTo("ownerId", currentUser.getUid())
                                    .get()
                                    .addOnSuccessListener(querySnapshot -> {
                                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                                            System.out.println("doc:" + doc);
                                        }
                                        storiesCount.setValue(querySnapshot.size());

                                        // Pobierz liczbę fragmentów
                                        db.collection("storyNodes")
                                                .whereEqualTo("authorId", currentUser.getUid())
                                                .get()
                                                .addOnSuccessListener(nodesSnapshot -> {
                                                    fragmentsCount.setValue(nodesSnapshot.size());
                                                    loading.setValue(false);
                                                })
                                                .addOnFailureListener(e -> {
                                                    error.setValue(e.getMessage());
                                                    loading.setValue(false);
                                                });

                                    })
                                    .addOnFailureListener(e -> {
                                        error.setValue(e.getMessage());
                                        loading.setValue(false);
                                    });

                        } else {
                            username.setValue("Unknown");
                            loading.setValue(false);
                        }
                    })
                    .addOnFailureListener(e -> {
                        error.setValue(e.getMessage());
                        loading.setValue(false);
                    });
        }

        public LiveData<String> getEmail() { return email; }
        public LiveData<String> getUsername() { return username; }
        public LiveData<Integer> getStoriesCount() { return storiesCount; }
        public LiveData<Integer> getFragmentsCount() { return fragmentsCount; }
        public LiveData<Boolean> getLoading() { return loading; }
        public LiveData<String> getError() { return error; }
    }
