package com.example.homin.test1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import android.service.notification.StatusBarNotification;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
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
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.example.homin.test1.WriteActivity.*;
import static com.example.homin.test1.ReadMemoActivity.*;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, MypageFragment.EssaySetlectedCallback {

    private boolean destinationClicked = false;


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
    private ImageView actionButton;
    private TextView wattingText;
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
    private Map<String, Bitmap> pictureList;
    private boolean check;
    private Location location;
    private String provider;
    private boolean zoomCheck;
    private boolean memoCheck;
    private List<ItemMemo> memoList;
    private List<ItemPerson> personMarkerList;
    private int pressedTime;
    private int sendTime;
    private Location getLocation;
    private long chatCheck;
    public static String MARKER_LIST = "markerList";
    private boolean checkLocation;
    private boolean destoryCheck;
    private boolean memoAddCheck;
    private boolean blueToothCheck;
    private int wattingCount;
    private boolean sendCheck;

    // MyPage에 이용
    private static final int CAMERA_CODE = 1000;
    private static final int GALLERY_CODE = 1001;
    private static final int CROP_IMAGE_CODE = 1002;
    //    private Uri filePath;
    private Uri photoUri, albumUri;
    private Uri selectedUri;
    Boolean albumPick = false;

    private String key;

    public static final String MEMOLIST = "memolistkeys";

    //자기위치로 되돌리는 버튼
    private FloatingActionButton selfLocationButton;

    //검색창
    private AutoCompleteTextView mSearchText;//검색창 뷰
    private PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace; // 자동검색창 각 리스트아이템 대한 정보
    private Button blutoothBtn;

    //목적지 설정
    private Marker mMarker; //목적지 마커(롱클릭시)
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));//지구전체 범위
    private View rootView;
    private TextView distanceIndicator; //거리표현하는 TextView;
    private ItemDestination targetMarker;//목적지 마커(검색시)
    private String targetId; //상대방을 목적지로 선택했을때
    private ItemPerson targetIdMarker;
    private View shapeView;
    private FloatingActionButton writeMemoButton;
    private Marker arrow;
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
                    bottomSheetBehavior.setState(bottomSheetBehavior.STATE_EXPANDED);

                    return true;

                case R.id.navigation_notifications:
                    FragmentManager manager2 = getSupportFragmentManager();
                    FragmentTransaction transaction2 = manager2.beginTransaction();
                    MypageFragment mypageFragment = new MypageFragment();
                    transaction2.replace(R.id.container_main, mypageFragment);
                    transaction2.commit();
                    bottomSheetBehavior.setState(bottomSheetBehavior.STATE_EXPANDED);

                    return true;
            }
            return false;
        }
    };

    private boolean isOnStop = false;
    @Override
    protected void onStop() {
        isOnStop = true;
        super.onStop();
    }

    @Override
    protected void onResume() {
        isOnStop = false;
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("dd4434", "MapsActivity onCreate");
        setContentView(R.layout.activity_maps);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorRealWhite, getResources().newTheme()));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        blutoothBtn = findViewById(R.id.button_blueTooth);
        rootView = findViewById(R.id.container);//Snackbar위한 View member변수
        Intent intent = new Intent(this, ClosingServics.class);
        startService(intent);

        context = getApplicationContext();
        Log.i("qq23q", "onCreate");
        memoList = new ArrayList<>();
        myFriendContactList = new ArrayList<>();
        personList = new HashMap<>();
        distanceIndicator = findViewById(R.id.distanceIndicator);
        shapeView = findViewById(R.id.shapeView);
        wattingText = findViewById(R.id.textView_watting);


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

        //메모쓰는 버튼
        writeMemoButton = findViewById(R.id.writeMemoButton);
        writeMemoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                writeMyLocation();
                Intent intent = new Intent(MapsActivity.this,WriteActivity.class);
                startActivityForResult(intent,RESULT_CODE);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        actionButton = findViewById(R.id.AddIndicator);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        FriendFragment friendFragment = new FriendFragment();
        transaction.replace(R.id.container_main, friendFragment);
        transaction.commit();


        bottomview = findViewById(R.id.bottom_sheet);
        actionLayout = findViewById(R.id.action_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomview);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    actionLayout.setVisibility(View.VISIBLE);
                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorRealWhite, getResources().newTheme()));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

                }
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    actionLayout.setVisibility(View.GONE);
                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorLightBlack, getResources().newTheme()));
                    getWindow().getDecorView().setSystemUiVisibility(0);
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


//

        if (placeInfo != null) {
            try {
                String snippet = "주소: " + placeInfo.getAddress() + "\n" +
                        "전화번호: " + placeInfo.getPhoneNumber() + "\n" +
                        "웹싸이트: " + placeInfo.getWebsiteUri() + "\n" +
                        "별점: " + placeInfo.getRating() + "\n";

                MarkerOptions options = new MarkerOptions().position(latLng).title(placeInfo.getName()).snippet(snippet);
                if (mMarker != null) {
                    mMarker.remove();
                    mMarker = null;
                }

                if (targetMarker != null) {
                    clusterManager.removeItem(targetMarker);
                    targetMarker = null;
                }
                targetMarker = new ItemDestination(latLng, placeInfo.getName(), snippet);
                clusterManager.addItem(targetMarker);
                clusterManager.cluster();

//                Collection<Marker> collection = clusterManager.getMarkerCollection().getMarkers();
//
//                for(Marker x: collection){
//                    Log.i("KSJ", "if문 들어옴111111");
//                    Log.i("KSJ","x:" + x.getTitle() +"\n" + "targetMarker: " + targetMarker.getTitle());
//                    if((Integer)x.getTag() == 3){
//                        Log.i("KSJ", "if문 들어옴222222");
//
//                        mMarker = x;
//                        Toast.makeText(context, x.getTitle(), Toast.LENGTH_SHORT).show();
//                    }
//                }
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
        Geocoder geocoder = new Geocoder(com.example.homin.test1.MapsActivity.this);

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
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(com.example.homin.test1.MapsActivity.this));

        reference = FirebaseDatabase.getInstance().getReference();


        if (clusterManager == null) {
            clusterManager = new ClusterManager<>(com.example.homin.test1.MapsActivity.this, mMap);
            clusterManager.setRenderer(new PersonItemRenderer(com.example.homin.test1.MapsActivity.this, mMap, clusterManager));
            clusterManager.setAlgorithm(new CustomAlgorithm<ClusterItem>());
            mMap.setOnCameraIdleListener(clusterManager);
            mMap.setOnMarkerClickListener(clusterManager);
            mMap.setOnInfoWindowClickListener(clusterManager);

        }


        clusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<ClusterItem>() {
            @Override
            public void onClusterItemInfoWindowClick(final ClusterItem clusterItem) {

                if (clusterItem instanceof ItemDestination) {

                    if (clusterItem.equals(targetMarker)) {

                        Snackbar.make(rootView, "목적지로 설정하시겠습니까?", 5000).setAction("네", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendCheck = true;
                                if (mMarker != null) {
                                    mMarker.remove();
                                    mMarker = null;

//                                    blutoothBtn.setVisibility(View.VISIBLE);
                                }

                                if (targetId!= null){
                                    targetId = null;
                                }

                                if(targetIdMarker!= null){
                                    targetIdMarker = null;
                                }
                                destinationClicked = true;
                                setDestination();
                            }
                        }).show();

                    }
                } else if (clusterItem instanceof ItemMemo) {

                    Snackbar.make(rootView, "전체내용을 보시겠습니까?", 5000).setAction("네", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(com.example.homin.test1.MapsActivity.this, ReadMemoActivity.class);
                            intent.putExtra(MEMO_NAME, ((ItemMemo) clusterItem).getUserName());
                            intent.putExtra(MEMO_ID, ((ItemMemo) clusterItem).getUserId());
                            intent.putExtra(MEMO_TITLE, ((ItemMemo) clusterItem).getTitle());
                            intent.putExtra(MEMO_CONTENT, ((ItemMemo) clusterItem).getContent());
                            intent.putExtra(MEMO_URL, ((ItemMemo) clusterItem).getImageUrl());
                            intent.putExtra(MEMO_TIME, ((ItemMemo) clusterItem).getTime());
                            startActivity(intent);

                        }
                    }).show();


                } else if (clusterItem instanceof ItemPerson) {
                    final String id = ((ItemPerson) clusterItem).getUserId();
                    if (id == DaoImple.getInstance().getContact().getUserId()) {

                    } else {
                        Snackbar.make(rootView, "목적지로 설정하시겠습니까?", 5000).setAction("네", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendCheck = true;
                                distanceIndicator.setText("위치 확인중");
                                if (mMarker != null) {
                                    mMarker.remove();
                                    mMarker = null;
                                }


                                destinationClicked = false;

                                if (targetMarker != null) {
                                    clusterManager.removeItem(targetMarker);
                                    clusterManager.cluster();
                                    targetMarker = null;
                                }


                                destinationClicked = false;
                                targetId = ((ItemPerson) clusterItem).getUserId();
                                setDestination();
                            }
                        }).show();

                    }
                }
            }
        });


        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<ClusterItem>() {
            @Override
            public boolean onClusterClick(Cluster<ClusterItem> cluster) {

                Collection<ClusterItem> clusters = cluster.getItems();
                List<ItemMemo> itemMemos = new ArrayList<>();
                List<ItemPerson> itemPeople = new ArrayList<>();
                for (ClusterItem m : clusters) {
                    if (m instanceof ItemMemo) {
                        itemMemos.add((ItemMemo) m);

                    } else if(m instanceof  ItemPerson) {
                        itemPeople.add((ItemPerson) m);
                    }
                }
                if (itemMemos.size() != 0) {
                    Intent intent = new Intent(com.example.homin.test1.MapsActivity.this, ItemDetailActivity.class);
                    intent.putExtra(MARKER_LIST, "memo");
                    DaoImple.getInstance().setItemMemoList(itemMemos);

                    startActivity(intent);
                } else {
                    Intent intent = new Intent(com.example.homin.test1.MapsActivity.this, ItemDetailActivity.class);
                    DaoImple.getInstance().setItemPersonList(itemPeople);
                    intent.putExtra(MARKER_LIST, "person");

                    startActivity(intent);
                }


                return true;
            }
        });



        myLocationUpdate(); // 내 위치 업데이트

        getFriendList(); // 친구 목록 가져오기





        // 친구 요청 Activity 실행
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(com.example.homin.test1.MapsActivity.this, WatingActivity.class);
                startActivity(intent1);

            }
        });


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {

                Snackbar.make(rootView, "목적지로 설정하시겠습니까?", 5000).setAction("네", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        distanceIndicator.setText("");
                        destinationClicked = true;
                        MarkerOptions options = new MarkerOptions().position(latLng).title("목적지").snippet("위도:" + latLng.latitude + "/n" + "경도: " + latLng.longitude);
                        if (mMarker != null) {
                            mMarker.remove();
                            mMarker = null;
                        }

                        if (targetMarker != null) {
                            clusterManager.removeItem(targetMarker);
                            clusterManager.cluster();
                            targetMarker = null;
                        }

                        if(targetId != null){
                            targetId = null;
                        }
                        mMarker = mMap.addMarker(options);
                        setDestination();

                    }
                }).show();

            }
        });

        // 친구 위치정보 받아오기
        reference.child("Contact").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (myFriendList != null) {
                    Contact contact = dataSnapshot.getValue(Contact.class);
                    if (contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())) {
                        myContact = contact;
                        Log.i("ddd3333", "콘텍트 생성");
                        DaoImple.getInstance().setContact(myContact);
                        if(contact.getWattingList() != null) {
                            Log.i("vv44","친구 신청");
                            wattingText.setText(String.valueOf(contact.getWattingList().size()));
                            actionButton.setImageResource(R.drawable.wattingy);

                        }else{
                            Log.i("vv44","친구 신청 없음");
                            wattingText.setText("");
                            actionButton.setImageResource(R.drawable.wattingn);
                        }
                    }
                    for (int a = 0; a < myFriendList.size(); a++) {
                        // 친구들 위치정보 받아와서 구글맵에 갱신
                        if (myFriendList.get(a).equals(contact.getUserId())) {
                            // 로그인 되어있는 상태라면 사용자 마커 표시
                            List<Double> friendLocation = contact.getUserLocation();
                            if (contact.getResizePictureUrl() != null) {
                                ItemPerson friendMarker = new ItemPerson(friendLocation.get(0),
                                        friendLocation.get(1), contact.getUserId(), contact.getUserName(), contact.getResizePictureUrl());
                                // 공개 여부 확인
                                if (contact.isPublic() || contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())) {
                                    // 로그인 상태 확인
                                    if (contact.isLoginCheck()) {
                                        clusterManager.addItem(friendMarker);
                                    }
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
                                        friendLocation.get(1), contact.getUserId(), contact.getUserName(), contact.getResizePictureUrl());
                                // 내 마커는 목적지 설정을 위해 멤버 변수에 저장
                                if (targetId != null) {
                                    if (contact.getUserId().equals(targetId)) {
                                        targetIdMarker = friendMarker;
                                    }
                                }
                                if (contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())) {
                                    myMarker = friendMarker;
                                    if (destinationClicked) {
                                        setDestination();
                                    }
                                    if (targetId != null) {
                                        setDestination();
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
            }

            // 친구 위치 바뀌었을때 정보 갱신
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                check = false;
                Log.i("fffff", "체인지 들어옴");
                reference.child("Contact").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Contact contact = dataSnapshot.getValue(Contact.class);
                        if (contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())) {
                            myContact = contact;
                            if(contact.getWattingList() != null) {
                                Log.i("vv44","친구 신청");
                                wattingText.setText(String.valueOf(contact.getWattingList().size()));
                                actionButton.setImageResource(R.drawable.wattingy);

                            }else{
                                Log.i("vv44","친구 신청 없음");
                                wattingText.setText("");
                                actionButton.setImageResource(R.drawable.wattingn);
                            }
                            List<String> realFriendList = contact.getFriendList();
                            myFriendList = new ArrayList<>();

                            for (int a = 0; a < realFriendList.size(); a++) {
                                String name = realFriendList.get(a);
                                myFriendList.add(name);
                            }
                            myFriendList.add(DaoImple.getInstance().getLoginEmail());
                            DaoImple.getInstance().setContact(contact);
                            Log.i("ddd3333", "콘텍트 생성");

                        }
                        if (myFriendList != null) {
                            for (int a = 0; a < myFriendList.size(); a++) {
                                Log.i("asdasd11", myFriendList.get(a));
                                if (myFriendList.get(a).equals(contact.getUserId())) {
                                    // 공개 여부 확인
                                    if (contact.isPublic() || contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())) {
                                        // 로그인 되있는 상태라면 사용자 마커 표시
                                        if (contact.isLoginCheck()) {
                                            Log.i("asdasd", "로그인 됨 : " + contact.getUserId());
                                            // 현재 저장 된 모든 마커 꺼내기
                                            Collection<ClusterItem> markers = clusterManager.getAlgorithm().getItems();
                                            // 저장 된 이름 정보와 firebase에 저장 된 이름 비교
                                            for (int b = 0; b < personList.size(); b++) {
                                                ClusterItem m = personList.get(contact.getUserId());
                                                if (m instanceof ItemPerson || m == null) {
                                                    if (m == null) {
                                                        List<Double> friendLocation = contact.getUserLocation();
                                                        if (contact.getResizePictureUrl() != null) {
                                                            ItemPerson friendMarker = new ItemPerson(friendLocation.get(0),
                                                                    friendLocation.get(1), contact.getUserId(), contact.getUserName(), contact.getResizePictureUrl());

                                                            clusterManager.addItem(friendMarker);
                                                            personList.put(contact.getUserId(), friendMarker);

                                                        } else {
                                                            BitmapFactory.Options options = new BitmapFactory.Options();
                                                            options.inSampleSize = 1;
                                                            Bitmap otherPicture = BitmapFactory.decodeResource(getResources(), R.drawable.what, options);
                                                            Bitmap picture = Bitmap.createScaledBitmap(otherPicture, 128, 128, true);
                                                            ItemPerson friendMarker = new ItemPerson(friendLocation.get(0),
                                                                    friendLocation.get(1), contact.getUserId(), contact.getUserName(), contact.getResizePictureUrl());
                                                            clusterManager.addItem(friendMarker);
                                                            personList.put(contact.getUserId(), friendMarker);
                                                        }

                                                    } else {
                                                        if (((ItemPerson) m).getUserId().equals(contact.getUserId())) {
                                                            ItemPerson ip = personList.get(contact.getUserId());
                                                            // 저장 되있는 Location 정보와 firebase에 저장된 Location 비교
                                                            LatLng saveLatLng = ip.getPosition();
                                                            String saveImage = "NoImage";
                                                            if(ip.getImage() != null) {
                                                                saveImage = ip.getImage();
                                                            }



                                                            if (targetId != null) {
                                                                if (contact.getUserId().equals(targetId)) {
                                                                    targetIdMarker = ip;
                                                                    setDestination();
                                                                }
                                                            }

                                                            if (contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())) {
                                                                myMarker = ip;
                                                                if (destinationClicked) {
                                                                    setDestination();
                                                                }
                                                            }

                                                            LatLng newLatLng = new LatLng(contact.getUserLocation().get(0),
                                                                    contact.getUserLocation().get(1));
                                                            if (saveLatLng.longitude != newLatLng.longitude ||
                                                                    saveLatLng.latitude != newLatLng.latitude || !saveImage.equals(contact.getPictureUrl())){
                                                                // 서로 다른 Location이 저장되 있다면, clusterManager에 저장된 마커 삭제

                                                                clusterManager.removeItem(ip);
                                                                personList.remove(contact.getUserId());
                                                                // 다시 마커 생성 후, clusterManager과 personList에 저장
                                                                List<Double> friendLocation = contact.getUserLocation();
                                                                if (contact.getResizePictureUrl() != null) {
                                                                    ItemPerson friendMarker = new ItemPerson(friendLocation.get(0),
                                                                            friendLocation.get(1), contact.getUserId(), contact.getUserName(), contact.getResizePictureUrl());

                                                                    clusterManager.addItem(friendMarker);
                                                                    personList.put(contact.getUserId(), friendMarker);
                                                                } else {
                                                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                                                    options.inSampleSize = 1;
                                                                    Bitmap otherPicture = BitmapFactory.decodeResource(getResources(), R.drawable.what, options);
                                                                    Bitmap picture = Bitmap.createScaledBitmap(otherPicture, 128, 128, true);
                                                                    ItemPerson friendMarker = new ItemPerson(friendLocation.get(0),
                                                                            friendLocation.get(1), contact.getUserId(), contact.getUserName(), contact.getResizePictureUrl());
                                                                    clusterManager.addItem(friendMarker);
                                                                    personList.put(contact.getUserId(), friendMarker);

                                                                }

                                                            }
                                                        }
                                                    }

                                                }

                                            }
                                        } else {
                                            Collection<ClusterItem> markers = clusterManager.getAlgorithm().getItems();
                                            for (ClusterItem m : markers) {
                                                if (m instanceof ItemPerson) {
                                                    if (((ItemPerson) m).getUserId().equals(contact.getUserId())) {
                                                        ItemPerson ip = personList.get(contact.getUserId());
                                                        clusterManager.removeItem(ip);
                                                    }
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

        // 채팅 수신 토스트로 보여주기
        reference.child("Chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                chatCheck = 0;
                Log.i("ddd66", "체인지");
                String key = dataSnapshot.getKey();
                int num = key.indexOf(DaoImple.getInstance().getKey());
                Log.i("ddd66", num + "");
                if (num != -1) {
                    long chatCount = dataSnapshot.getChildrenCount();
                    Log.i("ddd66", "들어옴");
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        chatCheck++;
                        Chat chat = d.getValue(Chat.class);
                        Log.i("ddd66", chat.getChat());
                        if (!(chat.getId().equals(DaoImple.getInstance().getLoginEmail()))) {
                            Log.i("ddd77", "count : " + chatCount + "," + "chat : " + chatCheck);
                            if (chatCount == chatCheck) {
                                Log.i("ddd66", chatCount + "");
                                Context activityCheck = DaoImple.getInstance().getChattingActivity();
                                if (activityCheck == null) {
                                    Log.i("ddd66", "체크" + activityCheck + "");
                                    if(isOnStop == true){
                                        Intent intent = new Intent(MapsActivity.this,NotificationService.class);
                                        intent.putExtra("name",chat.getName());
                                        intent.putExtra("chat",chat.getChat());
                                        intent.putExtra("id",chat.getId());
                                        intent.putExtra("type","msg");
                                        startService(intent);
                                    }else {

                                        Toast.makeText(context, chat.getName() + " : " + chat.getChat(), Toast.LENGTH_SHORT).show();
                                        chatCheck = 0;
                                    }
                                }
                            }
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


    // 내 친구 리스트 받아오고 친구 메모 가져오기
    private void getFriendList() {
        contactList = new ArrayList<>();
        reference.child("Contact").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Contact contact = dataSnapshot.getValue(Contact.class);
                contactList.add(contact);
                if (contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())) {
                    myContact = contact;
                    DaoImple.getInstance().setContact(contact);
                    List<Double> lastLocation = contact.getUserLocation();
                    LatLng latLng = new LatLng(lastLocation.get(0), lastLocation.get(1));
                    if (myLatLng == null) {
                        myLatLng = latLng;
                    }
                }
                if (contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())) {
                    if (contact.getFriendList() != null) {

                        List<String> fflist = contact.getFriendList(); // 친구 목록 저장
                        myFriendList = new ArrayList<>();
                        for (int a = 0; a < fflist.size(); a++) {
                            String name = fflist.get(a);
                            myFriendList.add(name);

                        }

                        myFriendList.add(DaoImple.getInstance().getLoginEmail());
//                        for (int a = 0; a < myFriendList.size(); a++) { //  친구 목록으로 메모 가져오기
//
//                            String key = DaoImple.getInstance().getFirebaseKey(myFriendList.get(a));
//                            friendMemeList(key); // 친구들 메모 가져오는 메소드
//
//
//                        }


                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Contact contact = dataSnapshot.getValue(Contact.class);
                if (contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())) {
                    if (contact.getFriendList() != null) {
                        myFriendList = contact.getFriendList();
                        List<String> fflist = contact.getFriendList(); // 친구 목록 저장
                        List<String> realFriendList = new ArrayList<>();
                        myContact = contact;
                        DaoImple.getInstance().setContact(contact);
                        for (int a = 0; a < fflist.size(); a++) {
                            String name = fflist.get(a);
                            realFriendList.add(name);
                        }

                        realFriendList.add(DaoImple.getInstance().getLoginEmail());

                        if(targetIdMarker!= null) {
                            clusterManager.addItem(targetIdMarker);
                            clusterManager.cluster();
                        }
                        if (!memoAddCheck) {
                            clusterManager.clearItems();
                            Log.i("dd4432", "메모 반복문 들어감");
                            for (int a = 0; a < realFriendList.size(); a++) { //  친구 목록으로 메모 가져오기
                                String key = DaoImple.getInstance().getFirebaseKey(realFriendList.get(a));
                                friendMemeList(key); // 친구들 메모 가져오는 메소드


                            }
                            memoAddCheck = true;
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
        memoList.clear();
        memoCheck = false;
        reference.child("userData").child(key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("asd13", "들어옴");
                UserDataTable data = dataSnapshot.getValue(UserDataTable.class);
                List<Double> friendLocation = data.getLocation();
                ItemMemo friendMemo = new ItemMemo(friendLocation.get(0), friendLocation.get(1),
                        data.getUserId(), data.getName(), data.getTitle(), data.getContent(),
                        data.getData(), data.getImageUrl(), BitmapFactory.decodeResource(context.getResources(), R.drawable.letter));
                if (!memoCheck) {
                    memoList.add(friendMemo);
                    //여기도 써놨자나영 !!
                    if(targetMarker!= null) {
                        clusterManager.clearItems();
                        clusterManager.addItem(targetMarker);
                        clusterManager.cluster();
                    }
                    memoCheck = true;
                }

                if (memoList.size() != 0) {
                    if (data.getContent().equals(memoList.get(memoList.size() - 1).getContent())
                            && data.getTitle().equals(memoList.get(memoList.size() - 1).getTitle()) &&
                            data.getData().equals(memoList.get(memoList.size() - 1).getTime())) {

                    } else {
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

//         나와 메모의 거리가 3km 미만이라면 메모 add
        float distance = myMemoLocation.distanceTo(yourMemoLocation);
        if (distance < 3000) {
            clusterManager.addItem(friendMemo);

            clusterManager.cluster();
        }


    }


    // 내 gps 위치 받아오고, firebase에 contact 업데이트
    @SuppressLint("MissingPermission")
    private void myLocationUpdate() {
        Log.i("asd123", "myLocationUpdate");
        if (locationManager == null) {
            locationManager = (LocationManager) this.getSystemService(context.LOCATION_SERVICE);
            Log.i("vvv456", "로케이션 매니저 생성");
        }
        Log.i("", "");

        // 최적 gps 하드웨어 검색
        Criteria c = new Criteria();
        provider = locationManager.getBestProvider(c, true);
        // 사용가능한 장치가 없다면 모든 장치에서 검색
        if (provider == null || !locationManager.isProviderEnabled(provider)) {
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
                if (myContact == null) {
                    myContact = DaoImple.getInstance().getContact();
                }
                Log.i("asdqwe", "로케이션 체인지");

                List<Double> myLocation = new ArrayList<>();
                myLocation.add(location.getLatitude());
                Log.i("ddd3333", location.getLongitude() + "");
                myLocation.add(location.getLongitude());
                myContact.setUserLocation(myLocation);
                reference.child("Contact").child(DaoImple.getInstance().getKey()).setValue(myContact);

                // 내 위치를 myLatLng로 생성
                myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                memoAddCheck = false;

                if (!zoomCheck) {
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
    void writeMyLocation() {
        // 현재 내 위치 가져오기
        reference.child("Contact").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                }
                Contact con = dataSnapshot.getValue(Contact.class);
                if (con.getUserId().equals(DaoImple.getInstance().getLoginEmail())) {
                    List<Double> location = con.getUserLocation();
                    LatLng myLL = new LatLng(location.get(0), location.get(1));
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

            switch (requestCode) {
                case RESULT_CODE:
                    Log.i("ggv", "onActivityResult");
                    String title = data.getStringExtra(TITLE_KEY);
                    String body = data.getStringExtra(BODY_KEY);
                    String time = data.getStringExtra(TIME_KEY);
                    String imageUrl = data.getStringExtra(IMAGEURL_KEY);
                    Log.i("ggv", "onActivityResult 데이터 뺌");
                    Log.i("gg", title + body);
                    if (!(title.equals("")) && !(body.equals(""))) {
                        // 클러스터 매니저에 메모 에드
                        LatLng memoLocation = DaoImple.getInstance().getWriteLocation();
                        ItemMemo myMemo = new ItemMemo(memoLocation.latitude, memoLocation.longitude,
                                DaoImple.getInstance().getLoginEmail(), DaoImple.getInstance().getLoginId(), title, body,
                                time, imageUrl, BitmapFactory.decodeResource(context.getResources(), R.drawable.letter));
                        Log.i("bb", "onActivityResult 내 메모 add");
                        // 파이어베이스에 메모 업로드
                        List<Double> tableLocation = new ArrayList<>();
                        tableLocation.add(memoLocation.latitude);
                        tableLocation.add(memoLocation.longitude);
                        UserDataTable table = new UserDataTable(DaoImple.getInstance().getLoginEmail(), DaoImple.getInstance().getLoginId()
                                , imageUrl, tableLocation, title, body, time);
                        reference.child("userData").child(DaoImple.getInstance().getKey()).push().setValue(table);
                        Log.i("ggv", "onActivityResult 파이어베이스 push()");

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
//                    if (check) {
//                        actionButton.setImageResource(R.drawable.ddww);
//
//                    } else {
//                        actionButton.setImageResource(R.drawable.ic_notifications_black_24dp);
//                    }

                    break;

                case REQUEST_ENABLE_BT:
                    if (resultCode == RESULT_OK) {
                        // 블루투스가 활성 상태로 변경됨
                        selectDevice();
                    } else if (resultCode == RESULT_CANCELED) {
                        // 블루투스가 비활성 상태임
                        Toast.makeText(context, "블루투스가 비활성화 상태 입니다.", Toast.LENGTH_SHORT).show();    // 어플리케이션 종료
                    }
                    break;

            } // end switch

        } // end if

        if (requestCode == 5004 && resultCode == 1) {
            final LatLng latLng = (LatLng) data.getParcelableExtra("LatLng");

            distanceIndicator.setText("");
            destinationClicked = true;
            MarkerOptions options = new MarkerOptions().position(latLng).title("목적지").snippet("위도:" + latLng.latitude + "/n" + "경도: " + latLng.longitude);
            if (mMarker != null) {
                mMarker.remove();
            }

            if (targetMarker != null) {
                clusterManager.removeItem(targetMarker);
                clusterManager.cluster();
                targetMarker = null;
            }
            mMarker = mMap.addMarker(options);
            setDestination();

            bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

        }
    } // onActivityResult()

    //EssayDetaliActivity의 인덱스갑 가져오는것
    @Override
    public void onessaySetlected(int position) {
        Intent intent = EssayDetailActivity.newIntent(this, position);
        startActivityForResult(intent, 5004);
    }


    @Override
    public void onBackPressed() {
        // 메신저창이 올라와 있는 상태에서 백키 누르면 메신저창은 내려감.
        if (bottomSheetBehavior.getState() == 3) {
            bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
            pressedTime = 0;
        } else if ((myMarker != null && mMarker != null) || (myMarker != null && targetMarker != null) || (myMarker != null && targetId != null)) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(MapsActivity.this);

            dialog.setTitle("목적지 취소여부")
                    .setMessage("선택된 목적지를 취소하시겠습니까?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            arrow.remove();
                            arrow = null;
                            blutoothBtn.setVisibility(View.GONE);
                            if (myMarker != null && mMarker != null) {
                                shapeView.setBackground(null);
                                distanceIndicator.setVisibility(View.GONE);
                                Toast.makeText(context, "목적지를 취소합니다", Toast.LENGTH_SHORT).show();
                                destinationClicked = false;

                                mMarker.remove();
                                mMarker = null;

                                String blueTooth = "0" + "\n" + "0";
                                sendData(blueTooth);
                            } else if (myMarker != null && targetMarker != null) {
                                shapeView.setBackground(null);
                                distanceIndicator.setVisibility(View.GONE);
                                distanceIndicator.setText("");
                                Toast.makeText(context, "목적지를 취소합니다", Toast.LENGTH_SHORT).show();
                                destinationClicked = false;
                                clusterManager.removeItem(targetMarker);
                                clusterManager.cluster();
                                targetMarker = null;

                                String blueTooth = "0" + "\n" + "0";
                                sendData(blueTooth);

                            } else if (myMarker != null && targetId != null) {
                                shapeView.setBackground(null);
                                distanceIndicator.setVisibility(View.GONE);
                                distanceIndicator.setText("");
                                Toast.makeText(context, "목적지를 취소합니다", Toast.LENGTH_SHORT).show();
                                destinationClicked = false;
                                targetId = null;
                                targetIdMarker = null;

                                String blueTooth = "0" + "\n" + "0";
                                sendData(blueTooth);
                            }
                        }
                    }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int i) {
                    dialoginterface.cancel();
                }
            });
            dialog.create().show();

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
        Intent intent = new Intent(MapsActivity.this,NotificationService.class);
        stopService(intent);


        reference.child("Contact").child(DaoImple.getInstance().getKey()).child("loginCheck").setValue(false);
        
        try {
//            mWorkerThread.interrupt();	// 데이터 수신 쓰레드 종료
//            mInputStream.close();
            mOutputStream.close();
            mSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    private Contact missLocation(Contact myContact) {
        List<Double> myLocation = myContact.getUserLocation();
        double lat = myLocation.get(0);
        double lon = myLocation.get(1);
        lat += 0.01;
        lon += 0.01;
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

    //클릭을 했으면 작동 백키를 눌렀으면 취소
    // 검색한 주소, 내가 임의로 설정한 위치, 내글에 대한 목적지 설정
    private void
    setDestination() {



        blutoothBtn.setVisibility(View.VISIBLE);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_final);
//        Bitmap bitmap1 = PersonItemRenderer.getCircleBitmap(bitmap);


        double degree = 0;
        if (mMarker != null) {
            degree = SphericalUtil.computeHeading(myMarker.getPosition(), mMarker.getPosition());
        } else if (targetMarker != null) {
            degree = SphericalUtil.computeHeading(myMarker.getPosition(), targetMarker.getPosition());
        } else if (targetIdMarker != null) {
            degree = SphericalUtil.computeHeading(myMarker.getPosition(), targetIdMarker.getPosition());
        }
        if (arrow != null) {
            arrow.remove();
            arrow = null;
        }


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.rotation((float) degree);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

        markerOptions.position(myMarker.getPosition());
        markerOptions.anchor(0.5f, 1.0f);
        arrow = mMap.addMarker(markerOptions);

        shapeView.setBackground(getDrawable(R.drawable.shape));
        distanceIndicator.setVisibility(View.VISIBLE);
        double km = 0;
//        Log.i("KSJ", myMarker.getPosition() + "||"+ mMarker.getPosition() + "");
        if (myMarker != null && mMarker != null) {
            double distance = SphericalUtil.computeDistanceBetween(myMarker.getPosition(), mMarker.getPosition());
            String stringDistance = Double.toString(distance);
            int index = stringDistance.indexOf(".");

            String m = stringDistance.substring(0, index);
            String cm = stringDistance.substring(index + 1, index + 3);
            km = Integer.parseInt(m) * 0.001;



            distanceIndicator.setText("목적지까지의 거리: " + km + "KM ");
            if(degree < 0) {
                degree = 360+degree;
            }

            String blueTooth = km + "\n" + degree;

            int second = (int) System.currentTimeMillis() - sendTime;
            if(sendCheck){
                sendData(blueTooth);
                Log.i("1234","처음 보냄 : " + blueTooth);
                sendTime = (int) System.currentTimeMillis();
                sendCheck = false;
            }
            if(index != 1) {
                if(second > 6000) {
                    sendData(blueTooth);
                    Log.i("1234","보냄 : " + blueTooth);
                    sendTime = (int) System.currentTimeMillis();
                }
            }





            if (distance < 100) {
                if(arrow!= null){
                    arrow.remove();
                    arrow = null;
                }
                shapeView.setBackground(null);
                distanceIndicator.setVisibility(View.GONE);
                Toast.makeText(context, "도착하였습니다", Toast.LENGTH_SHORT).show();
                destinationClicked = false;
                blutoothBtn.setVisibility(View.GONE);
                mMarker.remove();
                mMarker = null;

                String blueTooth1 = "0" + "\n" + "0";
                sendData(blueTooth1);

            }
        } else if (myMarker != null && targetMarker != null) {
//                clusterManager.addItem(targetMarker);
//                clusterManager.cluster();


            Log.i("KSJ", myMarker.getPosition() + "||" + targetMarker.getPosition() + "target?");
            double distance = SphericalUtil.computeDistanceBetween(myMarker.getPosition(), targetMarker.getPosition());
            String stringDistance = Double.toString(distance);
            int index = stringDistance.indexOf(".");
            String m = stringDistance.substring(0, index);
            String cm = stringDistance.substring(index + 1, index + 3);
            Log.i("KSJ", "distance: " + distance);
            km = Double.parseDouble(m) * 0.001d;
            Log.i("asdasd33",km+"");
            Log.i("asdasd33","index : " + index);


            distanceIndicator.setText("목적지까지의 거리: " + km + "KM ");

            if(degree < 0) {
                degree = 360+degree;
            }

            String blueTooth = km + "\n" + degree;
            Log.i("1234","1 : " + blueTooth);

            int second = (int) System.currentTimeMillis() - sendTime;
            if(sendCheck){
                sendData(blueTooth);
                Log.i("1234","처음 보냄 : " + blueTooth);
                sendTime = (int) System.currentTimeMillis();
                sendCheck = false;
            }
            if(index != 1) {
                if(second > 6000) {
                    sendData(blueTooth);
                    Log.i("1234","보냄 : " + blueTooth);
                    sendTime = (int) System.currentTimeMillis();
                }
            }


            if (distance < 100) {
                if(arrow!= null){
                    arrow.remove();
                    arrow = null;
                }
                shapeView.setBackground(null);
                distanceIndicator.setVisibility(View.GONE);
                distanceIndicator.setText("");
                Toast.makeText(context, "도착하였습니다", Toast.LENGTH_SHORT).show();
                destinationClicked = false;

                clusterManager.removeItem(targetMarker);
                clusterManager.cluster();
                targetMarker = null;

                String blueTooth1 = "0" + "\n" + "0";
                sendData(blueTooth1);

            }

        } else if (myMarker != null && targetId != null && targetIdMarker != null) {

            double distance = SphericalUtil.computeDistanceBetween(myMarker.getPosition(), targetIdMarker.getPosition());
            String stringDistance = Double.toString(distance);
            int index = stringDistance.indexOf(".");
            String m = stringDistance.substring(0, index);
            String cm = stringDistance.substring(index + 1, index + 3);
            Log.i("KSJ", "distance: " + distance);
            km = Double.parseDouble(m) * 0.001d;
            Log.i("asdasd33",km+"");

//            Collection<ClusterItem> items = clusterManager.getAlgorithm().getItems();
//            Iterator<ClusterItem> itemIterator = items.iterator();
//            while(itemIterator.hasNext()){
//                ClusterItem item = itemIterator.next();
//                if(item instanceof ItemPerson){
//                    if(((ItemPerson)item).getUserId() == targetId){
//
//                    }
//                }
//            }


            distanceIndicator.setText("목적지까지의 거리: " + km + "KM ");

            if(degree < 0) {
                degree = 360+degree;
            }

            String blueTooth = km + "\n" + degree;

            int second = (int) System.currentTimeMillis() - sendTime;
            if(sendCheck){
                sendData(blueTooth);
                sendTime = (int) System.currentTimeMillis();
                sendCheck = false;
            }
            if(index != 1) {
                if(second > 6000) {
                    sendData(blueTooth);
                    sendTime = (int) System.currentTimeMillis();
                }
            }


            if (distance < 100) {
                if(arrow!= null){
                    arrow.remove();
                    arrow = null;
                }
                shapeView.setBackground(null);
                distanceIndicator.setVisibility(View.GONE);
                distanceIndicator.setText("");
                Toast.makeText(context, "도착하였습니다", Toast.LENGTH_SHORT).show();
                destinationClicked = false;
                targetId = null;
                targetIdMarker = null;

                String blueTooth1 = "0" + "\n" + "0";
                sendData(blueTooth1);
            }

        }

        if(isOnStop == true){
            Log.i("KIMMY" , "들어옴?");
            Intent intent = new Intent(MapsActivity.this,NotificationService.class);
            intent.putExtra("name","목적지와의 거리");
            intent.putExtra("chat",km + "KM ");
            intent.putExtra("type","distance");
            startService(intent);

        }


    }


    // 블루투스 용 메소드와 멤버 변수
    private static final String TAG = "bluetooth.led";

    private static final int REQUEST_ENABLE_BT = 10;
    private static final String STRING_DELIMITER = "\n";
//    private static final char CHAR_DELIMITER = '\n';

    private BluetoothAdapter mBluetoothAdapter;
    private int mPairedDeviceCount = 0;
    private Set<BluetoothDevice> mDevices;
    private BluetoothDevice mRemoteDevice;
    private BluetoothSocket mSocket = null;
    private OutputStream mOutputStream = null;
    private ByteArrayOutputStream outputStream = null;
    private boolean checkBlue;



    void checkBluetooth(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null){
            // 장치가 블루투스를 지원하지 않는 경우
            Toast.makeText(context, "블루투스를 지원하지 않습니다.", Toast.LENGTH_SHORT).show();	// 어플리케이션 종료
        }
        else {
            // 장치가 블루투스를 지원하는 경우
            if (!mBluetoothAdapter.isEnabled()) {
                // 블루투스를 지원하지만 비활성 상태인 경우
                // 블루투스를 활성 상태로 바꾸기 위해 사용자 동의 요청
                Intent enableBtIntent =
                        new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else {
                // 블루투스를 지원하며 활성 상태인 경우
                // 페어링 된 기기 목록을 보여주고 연결할 장치를 선택
                selectDevice();
            }
        }
    }

    void selectDevice(){
        mDevices = mBluetoothAdapter.getBondedDevices();
        mPairedDeviceCount = mDevices.size();

        if(mPairedDeviceCount == 0){
            // 페어링 된 장치가 없는 경우
            Toast.makeText(context, "페어링 된 장치가 없습니다.", Toast.LENGTH_SHORT).show();		// 어플리케이션 종료
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("블루투스 장치 선택");

        // 페어링 된 블루투스 장치의 이름 목록 작성
        List<String> listItems = new ArrayList<String>();
        for (BluetoothDevice device : mDevices) {
            listItems.add(device.getName());
        }
        listItems.add("취소");		// 취소 항목 추가

        final CharSequence[] items =
                listItems.toArray(new CharSequence[listItems.size()]);

        builder.setItems(items, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int item){
                if(item == mPairedDeviceCount){
                    // 연결할 장치를 선택하지 않고 ‘취소’를 누른 경우
                    dialog.dismiss();
                }
                else{
                    // 연결할 장치를 선택한 경우
                    // 선택한 장치와 연결을 시도함
                    connectToSelectedDevice(items[item].toString());
                }
            }
        });

        builder.setCancelable(false);	// 뒤로 가기 버튼 사용 금지
        AlertDialog alert = builder.create();
        alert.show();
    }

    void sendData(String msg){
        try{
                msg += "\n";
                mOutputStream.write(msg.getBytes());


        }catch(Exception e){
            e.printStackTrace();
            // 문자열 전송 도중 오류가 발생한 경우
//            Toast.makeText(context, "블루투스 데이터 전송 오류 발생", Toast.LENGTH_SHORT).show();	// 어플리케이션 종료
        }
    }

    BluetoothDevice getDeviceFromBondedList(String name){
        BluetoothDevice selectedDevice = null;

        for (BluetoothDevice device : mDevices) {
            if(name.equals(device.getName())){
                selectedDevice = device;
                break;
            }
        }

        return selectedDevice;
    }

    void connectToSelectedDevice(String selectedDeviceName){
        mRemoteDevice = getDeviceFromBondedList(selectedDeviceName);
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try{
            // 소켓 생성
            mSocket = mRemoteDevice.createRfcommSocketToServiceRecord(uuid);
            // RFCOMM 채널을 통한 연결
            mSocket.connect();

            // 데이터 송수신을 위한 스트림 얻기
            mOutputStream = mSocket.getOutputStream();
//            mBufferedOutputStream = new BufferedOutputStream(mOutputStream);
//            mInputStream = mSocket.getInputStream();

            // 데이터 수신 준비
//            beginListenForData();
        }catch(Exception e){
            e.printStackTrace();
            // 블루투스 연결 중 오류 발생
            Toast.makeText(context, "블루투스 연결 중 오류 발생", Toast.LENGTH_SHORT).show();		// 어플리케이션 종료
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void blueToothOnclick(View view) {
        if(!checkBlue) {
            checkBluetooth();
            checkBlue = true;
            sendCheck = true;
        }

    }
}
