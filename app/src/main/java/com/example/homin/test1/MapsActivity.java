package com.example.homin.test1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;
//import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.homin.test1.WriteActivity.*;
import static com.example.homin.test1.ReadMemoActivity.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, MypageFragment.EssaySetlectedCallback {

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private static final int cameraZoom = 16;
    private GoogleMap mMap;
    private Contact myContact;
    private Context context;
    private MapView view;
    private static final int RESULT_CODE = 20;
    private LatLng addMakerLocation;
    private String email;
    //    private LinearLayout actionLayout;
    private StorageReference firebaseStorage;
    private FrameLayout actionLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    private View bottomview;
    private Menu mMenu;
    private FloatingActionButton actionButton;
    private DatabaseReference reference;
    private List<String> myFriendList;
    private List<String> memoFriendList;
    private Map<String, ItemPerson> personList;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng myLatLng;
    private ItemPerson myMarker;
    private ClusterManager<ClusterItem> clusterManager;
    private List<Contact> myFriendContactList;
    private List<Contact> contactList;
    private boolean check;
    private Location location;
    private String provider;
    private boolean zoomCheck;
    private boolean memoCheck;
    private List<ItemMemo> memoList;
    private List<ItemPerson> personMarkerList;
    private int pressedTime;
    private Location getLocation;
    public static String MARKER_LIST = "markerList";
    private boolean checkLocation;
    private boolean idCheck;



    // MyPage에 이용
    private static final int CAMERA_CODE = 1000;
    private static final int GALLERY_CODE = 1001;
    private static final int CROP_IMAGE_CODE = 1002;
//    private Uri filePath;
    private Uri photoUri, albumUri;
    private Uri selectedUri;
    Boolean albumPick = false;

    private String key;
    public static final String TAG = "mini";
    public static final String MEMOLIST = "memolistkeys";

    //자기위치로 되돌리는 버튼
    private FloatingActionButton selfLocationButton;

    //검색창
    private AutoCompleteTextView mSearchText;//검색창 뷰
    private PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    //    private GeoDataClient geoDataClient;
    private PlaceInfo mPlace; // 자동검색창 각 리스트아이템 대한 정보
    private Marker mMarker; //목적지 마커
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));//지구전체 범위

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    FriendFragment friendFragment = new FriendFragment();
                    transaction.replace(R.id.container_main, friendFragment);
                    transaction.commit();
                    return true;

                case R.id.navigation_dashboard:
                    FragmentManager manager1 = getSupportFragmentManager();
                    FragmentTransaction transaction1 = manager1.beginTransaction();
                    ChatListFragment chatListFragment = new ChatListFragment();
                    transaction1.replace(R.id.container_main, chatListFragment);
                    transaction1.commit();
                    return true;

                case R.id.navigation_notifications:
                    FragmentManager manager2 = getSupportFragmentManager();
                    FragmentTransaction transaction2 = manager2.beginTransaction();
                    MypageFragment mypageFragment = new MypageFragment();
                    transaction2.replace(R.id.container_main, mypageFragment);
                    transaction2.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = new Intent(this, ClosingServics.class);
        startService(intent);
        context = getApplicationContext();
        Log.i("qq23q", "onCreate");
        memoList = new ArrayList<>();
        myFriendContactList = new ArrayList<>();
        personList = new HashMap<>();


        //검색창 editText
        mSearchText = findViewById(R.id.input_search);

        //자기위치찾아주는 버튼 찾기
        selfLocationButton = findViewById(R.id.selfLocationIdentifier);
        selfLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 16));

            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        actionButton = findViewById(R.id.floatingActionButton);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        FriendFragment friendFragment = new FriendFragment();
        transaction.replace(R.id.container_main, friendFragment);
        transaction.commit();

        registerForContextMenu(findViewById(R.id.map));

        bottomview = findViewById(R.id.bottom_sheet);
        actionLayout = findViewById(R.id.action_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomview);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    actionLayout.setVisibility(View.VISIBLE);

                }
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    actionLayout.setVisibility(View.GONE);
                }

                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    actionLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }




    /********************************검색창을 위한 메소드들****************************************/
//목적지 설정후 목적지로 카메라 돌리기
    private void moveCamera(LatLng latLng, float zoom, String title) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions().position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }
        hideSoftKeyboard();
    }

    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));


        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker == mMarker) {
                    if (mMarker.isInfoWindowShown()) {
                        mMarker.hideInfoWindow();
                    } else {
                        mMarker.showInfoWindow();
                    }
                }
                return false;
            }
        });

        if (placeInfo != null) {
            try {
                String snippet = "주소: " + placeInfo.getAddress() + "\n" +
                        "전화번호: " + placeInfo.getPhoneNumber() + "\n" +
                        "웹싸이트: " + placeInfo.getWebsiteUri() + "\n" +
                        "별점: " + placeInfo.getRating() + "\n";

                MarkerOptions options = new MarkerOptions().position(latLng).title(placeInfo.getName()).snippet(snippet);
                mMarker = mMap.addMarker(options);
            } catch (NullPointerException e) {
                Log.e("bye", "moveCamera: " + e.getMessage());
            }
        } else {
            mMap.addMarker(new MarkerOptions().position(latLng));
        }



        hideSoftKeyboard();
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    //Google places API autocomplete suggestion

    private AdapterView.OnItemClickListener mAutoCompleteClickLister = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideSoftKeyboard();

            final AutocompletePrediction item = placeAutoCompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            PendingResult<PlaceBuffer> placeBufferPendingResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeBufferPendingResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.d("Bye", "onResult: Place query failed" + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);


            mPlace = new PlaceInfo();
            mPlace.setName(place.getName().toString());
            mPlace.setAddress(place.getAddress().toString());
//            mPlace.setAttribution(place.getAttributions().toString());
            mPlace.setId(place.getId());
            mPlace.setLatLng(place.getLatLng());
            mPlace.setRating(place.getRating());
            mPlace.setPhoneNumber(place.getPhoneNumber().toString());
            mPlace.setWebsiteUri(place.getWebsiteUri());

            Log.d("bye", mPlace.toString());

            moveCamera(new LatLng(place.getViewport().getCenter().latitude, place.getViewport().getCenter().longitude), 16, mPlace);

            places.release();
        }

    };

    private void init() {
        Log.d("bye", "init:initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        mSearchText.setOnItemClickListener(mAutoCompleteClickLister);

//        geoDataClient = Places.getGeoDataClient(this);

        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this, mGoogleApiClient, LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(placeAutoCompleteAdapter);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == event.ACTION_DOWN
                        || event.getAction() == event.KEYCODE_ENTER) {
                    geoLocate();
                }
                return false;
            }
        });
        hideSoftKeyboard();
    }

    private void geoLocate() {
        Log.d("bye", "geoLocate: geolocating");
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapsActivity.this);

        List<Address> list = new ArrayList<>();


        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e("bye", "geoLocate: Exception" + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            Log.i("bye", "geoLocate: found a location" + address.toString());
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

            moveCamera(latLng, 16, address.getAddressLine(0));
        }
        hideSoftKeyboard();
    }

    /********************************End of 검색창을 위한 메소드들****************************************/

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        init();
        email = DaoImple.getInstance().getLoginEmail();
        mMap = googleMap;

        reference = FirebaseDatabase.getInstance().getReference();


        if (clusterManager == null) {
            clusterManager = new ClusterManager<>(MapsActivity.this, mMap);
            clusterManager.setRenderer(new PersonItemRenderer(MapsActivity.this, mMap, clusterManager));
            clusterManager.setAlgorithm(new CustomAlgorithm<ClusterItem>());
            mMap.setOnCameraIdleListener(clusterManager);
            mMap.setOnMarkerClickListener(clusterManager);

        }


        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<ClusterItem>() {
            @Override
            public boolean onClusterClick(Cluster<ClusterItem> cluster) {

                Collection<ClusterItem> clusters = cluster.getItems();
                List<ItemMemo> itemMemos = new ArrayList<>();
                List<ItemPerson> itemPeople = new ArrayList<>();
                for (ClusterItem m : clusters) {
                    if (m instanceof ItemMemo) {
                        itemMemos.add((ItemMemo) m);
                        Log.i("ggqs", "메모 클릭");
                    } else {
                        itemPeople.add((ItemPerson) m);
                        Log.i("ggqs", "사람 클릭");
                    }
                }
                if (itemMemos.size() != 0) {
                    Intent intent = new Intent(MapsActivity.this, ItemDetailActivity.class);
                    intent.putExtra(MARKER_LIST, "memo");
                    DaoImple.getInstance().setItemMemoList(itemMemos);
                    Log.i("ggqs", itemMemos.size() + "");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MapsActivity.this, ItemDetailActivity.class);
                    DaoImple.getInstance().setItemPersonList(itemPeople);
                    intent.putExtra(MARKER_LIST, "person");
                    Log.i("ggqs", itemPeople.size() + "");
                    startActivity(intent);
                }





                return true;
            }
        });

        // 사람이나 메모 클릭시, 메모 마커는 메모 상세보기, 사람마커는 아직 미설정
        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ClusterItem>() {
            @Override
            public boolean onClusterItemClick(ClusterItem clusterItem) {
                if (clusterItem instanceof ItemMemo) {
                    Toast.makeText(context, "메모 클릭", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MapsActivity.this, ReadMemoActivity.class);
                    intent.putExtra(MEMO_NAME, ((ItemMemo) clusterItem).getUserName());
                    intent.putExtra(MEMO_ID, ((ItemMemo) clusterItem).getUserId());
                    intent.putExtra(MEMO_TITLE, ((ItemMemo) clusterItem).getTitle());
                    intent.putExtra(MEMO_CONTENT, ((ItemMemo) clusterItem).getContent());
                    intent.putExtra(MEMO_URL, ((ItemMemo) clusterItem).getImageUrl());
                    intent.putExtra(MEMO_TIME, ((ItemMemo) clusterItem).getTime());
                    startActivity(intent);

                } else {

                    Toast.makeText(context, "사람 클릭", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });


        Log.i("gg6", "클러스터 설정");
        Log.i("asd123", "onMapReady");
        myLocationUpdate(); // 내 위치 업데이트

        getFriendList(); // 친구 목록 가져오기


        Log.i("fffff", "체인지 끝남3");






        // 친구 요청 Activity 실행
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MapsActivity.this, WatingActivity.class);
                startActivity(intent1);

            }
        });


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                openContextMenu(findViewById(R.id.map));

            }
        });

        // 친구 위치정보 받아오기
        reference.child("Contact").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (myFriendList != null) {
                    Contact contact = dataSnapshot.getValue(Contact.class);
                    if(contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())){
                        myContact = contact;
                        Log.i("ddd3333","콘텍트 생성");
                        DaoImple.getInstance().setContact(myContact);
                    }
                    for(int a = 0 ; a < myFriendList.size() ; a++){
                        // 친구들 위치정보 받아와서 구글맵에 갱신
                        if(myFriendList.get(a).equals(contact.getUserId())) {
                            // 로그인 되어있는 상태라면 사용자 마커 표시
                                List<Double> friendLocation = contact.getUserLocation();
                                if (contact.getResizePictureUrl() != null) {
                                    ItemPerson friendMarker = new ItemPerson(friendLocation.get(0),
                                            friendLocation.get(1), contact.getUserId(), contact.getUserName(),contact.getResizePictureUrl());
                                    if (contact.isLoginCheck()) {
                                        clusterManager.addItem(friendMarker);
                                    }
                                    // 내 마커는 목적지 설정을 위해 멤버 변수에 저장
                                    if (contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())) {
                                        myMarker = friendMarker;
                                    }
                                    personList.put(contact.getUserId(), friendMarker);
                                    clusterManager.cluster();
//

                                } else {
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inSampleSize = 1;
                                    Bitmap otherPicture = BitmapFactory.decodeResource(getResources(), R.drawable.what, options);
                                    Bitmap picture = Bitmap.createScaledBitmap(otherPicture, 128, 128, true);
                                    ItemPerson friendMarker = new ItemPerson(friendLocation.get(0),
                                            friendLocation.get(1), contact.getUserId(), contact.getUserName(),contact.getResizePictureUrl());
                                    // 내 마커는 목적지 설정을 위해 멤버 변수에 저장
                                    if (contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())) {
                                        myMarker = friendMarker;
                                    }
                                    if (contact.isLoginCheck()) {
                                        clusterManager.addItem(friendMarker);
                                    }

                                    personList.put(contact.getUserId(), friendMarker);
                                    clusterManager.cluster();
//
                            }

                        }

                    }
                }
            }
             // 친구 위치 바뀌었을때 정보 갱신
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                check = false;
                Log.i("fffff","체인지 들어옴");
                reference.child("Contact").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Contact contact = dataSnapshot.getValue(Contact.class);
                        if(contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())){
                            myContact = contact;
                            List<String> realFriendList = contact.getFriendList();
                            myFriendList = new ArrayList<>();

                            for(int a = 0 ; a < realFriendList.size() ; a++){
                                String name = realFriendList.get(a);
                                myFriendList.add(name);
                            }
                            myFriendList.add(DaoImple.getInstance().getLoginEmail());
                            DaoImple.getInstance().setContact(contact);
                            Log.i("ddd3333","콘텍트 생성");

                        }
                        if(myFriendList != null){
                            for(int a = 0 ; a < myFriendList.size() ; a++){
                                Log.i("asdasd11",myFriendList.get(a));
                                if(myFriendList.get(a).equals(contact.getUserId())) {
                                    // 로그인 되있는 상태라면 사용자 마커 표시
                                    if (contact.isLoginCheck()) {
                                        Log.i("asdasd", "로그인 됨 : " + contact.getUserId());
                                        // 현재 저장 된 모든 마커 꺼내기
                                        Collection<ClusterItem> markers = clusterManager.getAlgorithm().getItems();
                                        // 저장 된 이름 정보와 firebase에 저장 된 이름 비교
                                        for (int b = 0; b < personList.size(); b++) {
                                            ClusterItem m = personList.get(contact.getUserId());
                                            if (m instanceof ItemPerson || m == null) {
                                                if(m == null){
                                                    List<Double> friendLocation = contact.getUserLocation();
                                                    if (contact.getResizePictureUrl() != null) {
                                                        ItemPerson friendMarker = new ItemPerson(friendLocation.get(0),
                                                                friendLocation.get(1), contact.getUserId(), contact.getUserName(),contact.getResizePictureUrl());

                                                        Log.i("fffff", "체인지 : 바뀐위치 저장");
                                                        clusterManager.addItem(friendMarker);
                                                        personList.put(contact.getUserId(), friendMarker);
                                                        Log.i("asdasd", "마커 생성");
                                                        Log.i("asdasd ", contact.getUserId());

                                                        Log.i("fffff", contact.getUserId());
                                                        Log.i("fffff", "체인지 : 친구위치 마커생성");
                                                        Log.i("fdfd", "마커 생성 체인지 : " + contact.getUserId());
                                                    } else {
                                                        BitmapFactory.Options options = new BitmapFactory.Options();
                                                        options.inSampleSize = 1;
                                                        Bitmap otherPicture = BitmapFactory.decodeResource(getResources(), R.drawable.what, options);
                                                        Bitmap picture = Bitmap.createScaledBitmap(otherPicture, 128, 128, true);
                                                        ItemPerson friendMarker = new ItemPerson(friendLocation.get(0),
                                                                friendLocation.get(1), contact.getUserId(), contact.getUserName(), contact.getResizePictureUrl());
                                                        clusterManager.addItem(friendMarker);
                                                        personList.put(contact.getUserId(), friendMarker);
                                                        Log.i("asdasd", "마커 생성");
                                                        Log.i("fdfd", "마커 생성 add : " + contact.getUserId());
                                                        Log.i("fffff", "체인지 else : " + contact.getUserId());
                                                    }

                                                }else {
                                                    Log.i("asdqwe", "사람임");
                                                    Log.i("asdqwe", "0   " + contact.getUserId());
                                                    if (((ItemPerson) m).getUserId().equals(contact.getUserId())) {
                                                        ItemPerson ip = personList.get(contact.getUserId());
                                                        Log.i("asdqwe", "1   " + contact.getUserId());
                                                        // 저장 되있는 Location 정보와 firebase에 저장된 Location 비교
                                                        LatLng saveLatLng = ip.getPosition();
                                                        if (contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())) {
                                                            myMarker = ip;
                                                        }
                                                        Log.i("fffff", "리스트 크기 : " + personList.size());
                                                        Log.i("fffff", "저장 : " + ip.getUserName());
                                                        LatLng newLatLng = new LatLng(contact.getUserLocation().get(0),
                                                                contact.getUserLocation().get(1));
                                                        Log.i("asdqwe", "2   " + contact.getUserId());
                                                        Log.i("qweasd", saveLatLng.toString() + contact.getUserId());
                                                        Log.i("qweasd", newLatLng.toString());
                                                        if (saveLatLng.longitude != newLatLng.longitude ||
                                                                saveLatLng.latitude != newLatLng.latitude) {
                                                            Log.i("asdqwe", "3   " + contact.getUserId());
                                                            Log.i("asdqwe", "체인지 : 위치 바뀜");
                                                            // 서로 다른 Location이 저장되 있다면, clusterManager에 저장된 마커 삭제
                                                            clusterManager.removeItem(ip);
                                                            personList.remove(contact.getUserId());
                                                            // 다시 마커 생성 후, clusterManager과 personList에 저장
                                                            List<Double> friendLocation = contact.getUserLocation();
                                                            if (contact.getResizePictureUrl() != null) {

                                                                ItemPerson friendMarker = new ItemPerson(friendLocation.get(0),
                                                                        friendLocation.get(1), contact.getUserId(), contact.getUserName(), contact.getResizePictureUrl());

                                                                Log.i("fffff", "체인지 : 바뀐위치 저장");
                                                                clusterManager.addItem(friendMarker);
                                                                personList.put(contact.getUserId(), friendMarker);
                                                                Log.i("asdasd", "마커 생성");
                                                                Log.i("asdasd ", contact.getUserId());

                                                                Log.i("fffff", contact.getUserId());
                                                                Log.i("fffff", "체인지 : 친구위치 마커생성");
                                                                Log.i("fdfd", "마커 생성 체인지 : " + contact.getUserId());
                                                            } else {
                                                                BitmapFactory.Options options = new BitmapFactory.Options();
                                                                options.inSampleSize = 1;
                                                                Bitmap otherPicture = BitmapFactory.decodeResource(getResources(), R.drawable.what, options);
                                                                Bitmap picture = Bitmap.createScaledBitmap(otherPicture, 128, 128, true);
                                                                ItemPerson friendMarker = new ItemPerson(friendLocation.get(0),
                                                                        friendLocation.get(1), contact.getUserId(), contact.getUserName(), contact.getResizePictureUrl());
                                                                clusterManager.addItem(friendMarker);
                                                                personList.put(contact.getUserId(), friendMarker);
                                                                Log.i("asdasd", "마커 생성");
                                                                Log.i("fdfd", "마커 생성 add : " + contact.getUserId());
                                                                Log.i("fffff", "체인지 else : " + contact.getUserId());


                                                            }

                                                        }
                                                    }
                                                }

                                            }

                                        }
                                    }else{
                                        Collection<ClusterItem> markers = clusterManager.getAlgorithm().getItems();
                                        for(ClusterItem m : markers){
                                            if(m instanceof ItemPerson){
                                              if(((ItemPerson)m).getUserId().equals(contact.getUserId())){
                                                  ItemPerson ip = personList.get(contact.getUserId());
                                                  clusterManager.removeItem(ip);

                                              }
                                            }
                                        }
                                    }
                                    }

                            }
                            clusterManager.cluster();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.i("fffff","체인지 끝남");
    }



    // 내 친구 리스트 받아오고 친구 메모 가져오기
    private void getFriendList() {
        contactList = new ArrayList<>();
        reference.child("Contact").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Contact contact = dataSnapshot.getValue(Contact.class);
                contactList.add(contact);
                if(contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())){
                    myContact = contact;
                    DaoImple.getInstance().setContact(contact);
                    List<Double> lastLocation = contact.getUserLocation();
                    LatLng latLng = new LatLng(lastLocation.get(0),lastLocation.get(1));
                    myLatLng = latLng;
                }
                if(contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())){
                    if(contact.getFriendList() != null) {

                        List<String> fflist = contact.getFriendList(); // 친구 목록 저장
                        myFriendList = new ArrayList<>();
                        for(int a = 0 ; a < fflist.size() ; a++){
                            String name = fflist.get(a);
                            myFriendList.add(name);

                        }

                        myFriendList.add(DaoImple.getInstance().getLoginEmail());
                        Log.i("fffff","친구 목록 저장");
                        if(!memoCheck) {
                            Log.i("fffff", "친구 메모 삭제");
                            for (int a = 0; a < myFriendList.size(); a++) { //  친구 목록으로 메모 가져오기
                                String key = DaoImple.getInstance().getFirebaseKey(myFriendList.get(a));
                                Log.i("asd13", "에드에서 부름");
                                friendMemeList(key); // 친구들 메모 가져오는 메소드
                            }
                        }


                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Contact contact = dataSnapshot.getValue(Contact.class);
                contactList.clear();
                contactList.add(contact);
                if(contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())){
                    if(contact.getFriendList() != null) {
                        myFriendList = contact.getFriendList();
                        List<String> fflist = contact.getFriendList(); // 친구 목록 저장
                        List<String> realFriendList = new ArrayList<>();
                        myContact = contact;
                        DaoImple.getInstance().setContact(contact);
                        for(int a = 0 ; a < fflist.size() ; a++){
                            String name = fflist.get(a);
                            realFriendList.add(name);
                        }

                        realFriendList.add(DaoImple.getInstance().getLoginEmail());
                        clusterManager.clearItems();
                        for(int a = 0 ; a < realFriendList.size() ; a++) { //  친구 목록으로 메모 가져오기

                            String key = DaoImple.getInstance().getFirebaseKey(realFriendList.get(a));
                            friendMemeList(key); // 친구들 메모 가져오는 메소드
                            Log.i("asd13", "체인지에서 부름");
                        }
                        }
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }

    // 친구 메모리스트 받아오기
    private void friendMemeList(String key) {

        reference.child("userData").child(key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("asd13", "들어옴");
                UserDataTable data = dataSnapshot.getValue(UserDataTable.class);
                List<Double> friendLocation = data.getLocation();
                ItemMemo friendMemo = new ItemMemo(friendLocation.get(0),friendLocation.get(1),
                        data.getUserId(),data.getName(),data.getTitle(),data.getContent(),
                        data.getData(),data.getImageUrl(),BitmapFactory.decodeResource(context.getResources(),R.drawable.letter));
                if(!memoCheck) {
                    memoList.add(friendMemo);
                    memoCheck = true;
                }

                if(memoList.size() != 0){
                    if(data.getContent().equals(memoList.get(memoList.size()-1).getContent())
                            && data.getTitle().equals(memoList.get(memoList.size()-1).getTitle()) &&
                            data.getData().equals(memoList.get(memoList.size()-1).getTime())){

                    }else{
                        memoList.add(friendMemo);
                        // 메모의 거리를 계산 해주는 메소드
                        memoDistanceAdd(friendMemo);
                        Log.i("asd13", "메모 생성");
                    }
                }
                clusterManager.cluster();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // 메모와의 거리를 계산해주는 메소드
    private void memoDistanceAdd(ItemMemo friendMemo) {
                Location myMemoLocation = new Location("my");
                myMemoLocation.setLatitude(myLatLng.latitude);
                myMemoLocation.setLatitude(myLatLng.longitude);
                Location yourMemoLocation = new Location("your");
                yourMemoLocation.setLatitude(friendMemo.getPosition().latitude);
                yourMemoLocation.setLatitude(friendMemo.getPosition().longitude);

//         나와 메모의 거리가 300m 미만이라면 메모 add
                float distance = yourMemoLocation.distanceTo(myMemoLocation);
                if(distance < 300) {
                    clusterManager.addItem(friendMemo);
                    Collection<ClusterItem> collection = clusterManager.getAlgorithm().getItems();
                    for(ClusterItem m : collection){
                        if(m instanceof ItemMemo){
                            Log.i("asdfff",((ItemMemo)m).getContent());
                            Log.i("asdfff", "distance : " + distance);
                        }
                    }
                    Log.i("aaa1234", friendMemo.getTitle());
                    Log.i("aaa1234", "친구 메모 에드");
                }
//
    }

    // 내 gps 위치 받아오고, firebase에 contact 업데이트
    @SuppressLint("MissingPermission")
    private void myLocationUpdate() {
        Log.i("asd123","myLocationUpdate");
        if (locationManager == null) {
            locationManager = (LocationManager) this.getSystemService(context.LOCATION_SERVICE);
            Log.i("vvv456","로케이션 매니저 생성");
        }
        Log.i("","");

        // 최적 gps 하드웨어 검색
        Criteria c = new Criteria();
        provider = locationManager.getBestProvider(c,true);
        // 사용가능한 장치가 없다면 모든 장치에서 검색
        if(provider == null || !locationManager.isProviderEnabled(provider)) {
            List<String> hardWare = locationManager.getAllProviders();
            for (int a = 0; a < hardWare.size(); a++) {
                String gpsHardware = hardWare.get(a);
                if (locationManager.isProviderEnabled(gpsHardware)) {
                    provider = gpsHardware;
                    break;
                }
            }
        }


            // 내 GPS 위치가 바뀔 때 마다, 내 마커 생성
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    check = false;
                    if(myContact == null) {
                        myContact = DaoImple.getInstance().getContact();
                    }
                    Log.i("asdqwe","로케이션 체인지");

                    List<Double> myLocation = new ArrayList<>();
                    myLocation.add(location.getLatitude());
                    Log.i("ddd3333",location.getLongitude()+"");
                    myLocation.add(location.getLongitude());
                    myContact.setUserLocation(myLocation);
                    reference.child("Contact").child(DaoImple.getInstance().getKey()).setValue(myContact);

                    // 내 위치를 myLatLng로 생성
                        myLatLng = new LatLng(location.getLatitude(), location.getLongitude());



                    if(!zoomCheck) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, cameraZoom));
                        zoomCheck = true;
                        clusterManager.cluster();
                    }



                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListener);



    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 1, 0, "글 남기기");
        menu.add(0, 2, 0, "목적지로 설정하기");
        menu.add(0, 3, 0, "취소");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                writeMyLocation();
                Intent intent = new Intent(MapsActivity.this,WriteActivity.class);
                startActivityForResult(intent,RESULT_CODE);
                break;
            case 2:
                break;
            case 3:
                closeContextMenu();
                break;
        }
        return super.onContextItemSelected(item);
    }

//    // 갤러리에서 사진 선택하는 메소드
//    public void clickedProImgBotton() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        //TODO: ACTION_PICK(이미지가 저장되어있는 폴더를 선택) ACTION_GET_CONTENT(전체 이미지를 폴더 구분없이 최신 이미지 순)랑 둘 비교
//        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        startActivityForResult(intent, GALLERY_CODE);
//        Log.i(TAG, "갤러리 코드: " + intent);
//    } // end clickedProImgBotton()
//
//
//    // 팝업뜰때 카메라 눌렀을때 발생하는 메소드  속에 내부메소드!
//    public void popupCameraInCameraMethod() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            Log.i(TAG, "intent.getData(): " + intent.getData());
//            startActivityForResult(intent, CAMERA_CODE);
//
//            Log.i(TAG, "팝업창에서 카메라 눌른후");
//        }
//    }




    @SuppressLint("MissingPermission")
    void writeMyLocation(){
        // 현재 내 위치 가져오기
        reference.child("Contact").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot data : dataSnapshot.getChildren()){

                }
                Contact con = dataSnapshot.getValue(Contact.class);
                if(con.getUserId().equals(DaoImple.getInstance().getLoginEmail())){
                    List<Double> location = con.getUserLocation();
                    LatLng myLL = new LatLng(location.get(0),location.get(1));
                    DaoImple.getInstance().setWriteLocation(myLL);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // 현재 gps 위치 저장


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("ggv", "onActivityResult 들어옴");
        // WriteActivity에서 받아온 글 정보들을 마커로 생성
        if (resultCode == RESULT_OK) {
            Log.i(TAG, "RESULT_OK");
            switch (requestCode) {
                case RESULT_CODE:
                    Log.i("ggv","onActivityResult");
                    String title = data.getStringExtra(TITLE_KEY);
                    String body = data.getStringExtra(BODY_KEY);
                    String time = data.getStringExtra(TIME_KEY);
                    String imageUrl = data.getStringExtra(IMAGEURL_KEY);
                    Log.i("ggv","onActivityResult 데이터 뺌");
                    Log.i("gg", title + body);
                    if (!(title.equals("")) && !(body.equals(""))) {
                        // 클러스터 매니저에 메모 에드
                        LatLng memoLocation = DaoImple.getInstance().getWriteLocation();
                        ItemMemo myMemo = new ItemMemo(memoLocation.latitude,memoLocation.longitude,
                                DaoImple.getInstance().getLoginEmail(),DaoImple.getInstance().getLoginId(),title,body,
                                time,imageUrl,BitmapFactory.decodeResource(context.getResources(),R.drawable.letter));
                        Log.i("bb","onActivityResult 내 메모 add");
                        // 파이어베이스에 메모 업로드
                        List<Double> tableLocation = new ArrayList<>();
                        tableLocation.add(memoLocation.latitude);
                        tableLocation.add(memoLocation.longitude);
                        UserDataTable table = new UserDataTable(DaoImple.getInstance().getLoginEmail(),DaoImple.getInstance().getLoginId()
                                ,imageUrl,tableLocation,title,body,time);
                        reference.child("userData").child(DaoImple.getInstance().getKey()).push().setValue(table);
                        Log.i("ggv","onActivityResult 파이어베이스 push()");

                    }
                    break;

                case GALLERY_CODE: // 갤러리에서 선택한 사진처리
                    albumPick = true;
                    File albumFile = null;
                    try {
                        albumFile = MypageFragment.createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (albumFile != null) {
//                        albumUri = Uri.fromFile(albumFile);
                        albumUri = FileProvider.getUriForFile(this, getPackageName(), albumFile);
                    }

                    photoUri = data.getData(); // 선택한 사진 Uri 정보

                case CAMERA_CODE: // 팝업창에서 카메라 버튼 클릭
//                    ProgressDialog progressDialog = new ProgressDialog(this);
//                    progressDialog.setMessage("처리 중...");
//                    progressDialog.show();

                    cropImage();

//                    progressDialog.dismiss();
                    break;

                case CROP_IMAGE_CODE: // user가 지정한 사진 설정 처리
//                    Bitmap cropImg = BitmapFactory.decodeFile(photoUri.getPath());

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE); // 동기화?
                    if (albumPick == false) {
                        mediaScanIntent.setData(photoUri);

                    } else if (albumPick == true) {
                        albumPick = false;
                        mediaScanIntent.setData(albumUri);

                    }
                    this.sendBroadcast(mediaScanIntent);

                    MypageFragment.uploadFile(selectedUri);
                    MypageFragment.resizeImg(selectedUri);

                    break;

                case 400:

                    boolean check = data.getBooleanExtra("check", false);
                    if (check) {
                        actionButton.setImageResource(R.drawable.ddww);
                    } else {
                        actionButton.setImageResource(R.drawable.ic_notifications_black_24dp);
                    }

                    break;

            } // end switch

        } // end if
    } // onActivityResult()

    //EssayDetaliActivity의 인덱스갑 가져오는것
    @Override
    public void onessaySetlected(int position) {
        Intent intent = EssayDetailActivity.newIntent(this,position);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        // 메신저창이 올라와 있는 상태에서 백키 누르면 메신저창은 내려감.
        if (bottomSheetBehavior.getState() == 3) {
            bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
            pressedTime = 0;
        } else {
            // 백키를 두번 눌렀을때, 그 간격이 2초 이하면 어플 종료
            if (pressedTime == 0) {
                Toast.makeText(context, "한번 더 누르면 종료 됩니다.", Toast.LENGTH_SHORT).show();
                pressedTime = (int) System.currentTimeMillis();
            } else {
                int second = (int) (System.currentTimeMillis() - pressedTime);
                if (second > 2000) {
                    Toast.makeText(context, "한번 더 누르면 종료 됩니다.", Toast.LENGTH_SHORT).show();
                    pressedTime = 0;
                } else {
                    Contact myContact = DaoImple.getInstance().getContact();
                    reference.child("Contact").child(DaoImple.getInstance().getKey()).setValue(myContact);
                    finishAffinity();

                }
            }
        }

    }



    @Override
    protected void onDestroy() {

        Contact asd = DaoImple.getInstance().getContact();
        asd.setLoginCheck(false);

            reference.child("Contact").child(DaoImple.getInstance().getKey()).setValue(asd);

        super.onDestroy();
    }


    private Contact missLocation(Contact myContact) {
        List<Double> myLocation = myContact.getUserLocation();
        double lat = myLocation.get(0);
        double lon = myLocation.get(1);
        lat+=0.01;
        lon+=0.01;
        myLocation.clear();
        myLocation.add(lat);
        myLocation.add(lon);
        return myContact;
    }


    // 갤러리에서 사진 선택하는 메소드
    public void clickedProImgBotton() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        //TODO: ACTION_PICK(이미지가 저장되어있는 폴더를 선택) ACTION_GET_CONTENT(전체 이미지를 폴더 구분없이 최신 이미지 순)랑 둘 비교
//        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        intent.setType(MediaStore.Images.Media.CONTENT_TYPE); // ?? 이렇게 하면 어떻게 나오지

//        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GALLERY_CODE);
    } // end clickedProImgBotton()


    // 프로필 사진 직접 촬영하는 메소드
    public void popupCameraInCameraMethod() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = MypageFragment.createImageFile(); // 원본 이미지 파일 저장 폴더 생성
                } catch (IOException ex) {
                    Toast.makeText(this, "createImageFile 실패", Toast.LENGTH_LONG).show();
                }

                if (photoFile != null) {
//                    photoUri = Uri.fromFile(photoFile); // 원본 파일 경로 받아옴
                    photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri); // 경로에 저장

                    startActivityForResult(takePictureIntent, CAMERA_CODE);
                }

            }
        }
    } // end popupCameraInCameraMethod()


    // 원본 이미지 crop하는 메소드
    private void cropImage() {

        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        cropIntent.setDataAndType(photoUri, "image/*");
//        cropIntent.putExtra("outputX", 200); // crop한 이미지의 x축 크기
//        cropIntent.putExtra("outputY", 200); // crop한 이미지의 y축 크기
//        cropIntent.putExtra("aspectX", 1); // crop 박스의 x축 비율
//        cropIntent.putExtra("aspectY", 1); // crop 박스의 y축 비율
        cropIntent.putExtra("scale", true);

        if (albumPick == false) {
            selectedUri = photoUri;
            cropIntent.putExtra("output", selectedUri);

        } else if (albumPick == true) {
            selectedUri = albumUri;
            cropIntent.putExtra("output", selectedUri);

        }

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(cropIntent, 0);
        grantUriPermission(list.get(0).activityInfo.packageName, selectedUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent in = new Intent(cropIntent);
        ResolveInfo res = list.get(0);
        in.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        in.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        grantUriPermission(res.activityInfo.packageName, selectedUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        in.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

        startActivityForResult(in, CROP_IMAGE_CODE);

    } // end cropImage()


}
