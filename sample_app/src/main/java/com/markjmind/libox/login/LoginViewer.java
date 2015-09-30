package com.markjmind.libox.login;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.markjmind.libox.MainViewer;
import com.markjmind.libox.R;
import com.markjmind.libox.common.Web;
import com.markjmind.uni.UpdateEvent;
import com.markjmind.uni.Viewer;
import com.markjmind.uni.annotiation.GetView;
import com.markjmind.uni.annotiation.Layout;
import com.markjmind.uni.hub.Store;

import java.io.IOException;

/**
 * Created by codemasta on 2015-09-15.
 */
@Layout(R.layout.view_login)
public class LoginViewer extends Viewer {

    @GetView
    private EditText editId,editPass;
    @GetView
    private Button loginBtn;
    @GetView
    private View findIdPwBtn;



    String result=null;
    Store<String> webParam = new Store<String>();

    @Override
    public boolean onLoad(int requestCode, UpdateEvent event) {
        if(requestCode==1) {
            Web web = new Web();
            try {
                result = web.addParam(webParam).postDM("IF-HLO-DM-0001");
                webParam.clear();
                return true;
            } catch (IOException e) {
                return false;
            }
        }else {
            return true;
        }
    }


    @Override
    public void onPost(int requestCode) {
        if(requestCode==1){
            Toast.makeText(getContext(),"로그인 되었습니다.",Toast.LENGTH_SHORT);
            Viewer.build(MainViewer.class, getActivity()).change(this);
        }else{
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webParam.add("loginId", editId.getText().toString())
                            .add("userPw", editPass.getText().toString())
                            .add("reqDate", "20150916095826")
                            .add("registrationId", "");
                    runLoad(1);
                }
            });
        }







        result=null;
    }

    @Override
    public void onFail(Integer requestCode) {
        Toast.makeText(getContext(),"로그인 실패",Toast.LENGTH_SHORT);
    }
}
