package eu.mobiletouch.fotoland.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import eu.mobiletouch.fotoland.R;
import eu.mobiletouch.fotoland.utils.ImeUtils;

/**
 * Created on 21-Sep-16.
 */
public class DialogAddText extends DialogFragment {
    private static final String ARG_TEXT = "argText";

    private String mText;
    private OnAddTextListener mOnAddTextListener;

    public static DialogAddText newInstance(String text) {
        DialogAddText frag = new DialogAddText();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mText = getArguments().getString(ARG_TEXT, "");
        setCancelable(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_text, container, false);
        Button btnAdd = (Button) view.findViewById(R.id.btn_addText);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        final EditText edtInput = (EditText) view.findViewById(R.id.edt_input);
        edtInput.setText(mText);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAddTextListener != null) {
                    mOnAddTextListener.onAdd(edtInput.getText().toString());
                }
                ImeUtils.hideIme(edtInput);
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImeUtils.hideIme(edtInput);
                dismiss();
            }
        });

        ImeUtils.showIme(edtInput);

        return view;
    }

    public DialogAddText setOnAddTextListener(OnAddTextListener onAddTextListener) {
        mOnAddTextListener = onAddTextListener;
        return this;
    }

    public interface OnAddTextListener {
        void onAdd(String text);
    }

}
