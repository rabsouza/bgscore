package br.com.battista.bgscore.repository;

import android.content.Context;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.model.dto.AvatarDto;
import br.com.battista.bgscore.model.enuns.AvatarEnum;
import br.com.battista.bgscore.util.LogUtils;

public class AvatarRepository {

    private static final String TAG = AvatarRepository.class.getSimpleName();
    private static final Set<AvatarDto> avatars = Sets.newLinkedHashSet();

    public AvatarRepository() {
        if (avatars.isEmpty()) {
            initAvatars();
        }
    }

    private void initAvatars() {
        final Context context = MainApplication.instance().getApplicationContext();
        for (AvatarEnum avatar : AvatarEnum.values()) {
            avatars.add(new AvatarDto()
                    .idResAvatar(avatar.getIdResDrawable())
                    .nameAvatar(context.getString(avatar.getIdResString())));
        }
    }

    public List<AvatarDto> findAll() {
        LogUtils.i(TAG, "Find all Avatars.");

        final List<AvatarDto> avatarDtos = Lists.newLinkedList(avatars);
        Collections.sort(avatarDtos, new Ordering<AvatarDto>() {
            @Override
            public int compare(AvatarDto left, AvatarDto right) {
                return left.compareTo(right);
            }
        });
        return avatarDtos;
    }

}
