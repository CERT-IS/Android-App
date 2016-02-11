package com.certis;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class NavigationDrawerActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private NavigationView navigationView; // gradle에서 design 라이브러리를 추가 해줘야한다.
    private DrawerLayout drawerLayout;
    protected FrameLayout main_frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // layout_toolbar.xml에서 정의한 툴바를 설정한다.
//        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기존의 액션바 타이틀을 없앤다.

        main_frame = (FrameLayout) findViewById(R.id.main_frame);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        /**
         * 네비게이션 드로어 영역의 아이템 선택 리스너
         */
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            //private Intent intent;

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }

                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
//                    sub_header_notice
                    case R.id.sub_item_notice:
                        Intent intent = new Intent(getApplication(), NoticeActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.sub_item_minutes:
                        Toast.makeText(getApplication(), "회의록", Toast.LENGTH_LONG).show();
                        return true;

                    case R.id.sub_item_link_board:
                        intent = new Intent(getApplication(), LinkActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.sub_item_calender:
                        intent = new Intent(getApplication(), CalendarActivity.class);
                        startActivity(intent);
                        return true;

//                    sub_header_about
                    case R.id.sub_item_about:
                        Toast.makeText(getApplication(), "동아리 소개", Toast.LENGTH_LONG).show();
                        return true;

//                    sub_header_data
                    case R.id.sub_item_gallery:
                        intent = new Intent(getApplication(), GalleryActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.sub_item_data_board:
                        Toast.makeText(getApplication(), "자료 게시판", Toast.LENGTH_LONG).show();
                        return true;

//                    sub_header_free_board
                    case R.id.sub_item_free_board:
                        Toast.makeText(getApplication(), "자유 게시판", Toast.LENGTH_LONG).show();
                        return true;

//                    sub_header_book
                    case R.id.sub_item_book:
                        Toast.makeText(getApplication(), "도서", Toast.LENGTH_LONG).show();
                        return true;

//                    sub_header_team_project
                    case R.id.sub_item_team_project:
                        intent = new Intent(getApplication(), TeamActivity.class);
                        startActivity(intent);
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        /**
         * 액션바드로어토클을 이용해 네비게이션 드로어 메뉴, 뒤돌아가기 이벤트 추가.
         */
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        /**
         * 툴바의 가운데 영역인 앱 타이틀 및 로고 영역 클릭시 이벤트
         */
        toolbar.findViewById(R.id.toolbarCenter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), IndexActivity.class);
                // 액티비티 스택 관련
                // (스택 최상위에서 아래로 가장 가까운 메인을 부른 후 그 사이의 스택 제거)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

                Toast.makeText(getApplication(), "메인(index)으로 이동", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Toast.makeText(getApplication(), "툴바 검색", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
