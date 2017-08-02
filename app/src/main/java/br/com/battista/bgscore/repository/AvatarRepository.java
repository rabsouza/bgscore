package br.com.battista.bgscore.repository;

import android.content.Context;
import android.util.Log;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.model.dto.AvatarDto;

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

        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_profile)
                .nameAvatar(context.getString(R.string.text_default_username)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_boba_fett)
                .nameAvatar(context.getString(R.string.avatar_boba_fett)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_fett_jango)
                .nameAvatar(context.getString(R.string.avatar_feet_jango)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_jawa)
                .nameAvatar(context.getString(R.string.avatar_jawa)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_leia_princess)
                .nameAvatar(context.getString(R.string.avatar_leia_princess)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_lobot)
                .nameAvatar(context.getString(R.string.avatar_lobot)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_r2d2)
                .nameAvatar(context.getString(R.string.avatar_r2d2)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_ackbar_admiral)
                .nameAvatar(context.getString(R.string.avatar_ackbar_admiral)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_c3p0)
                .nameAvatar(context.getString(R.string.avatar_c3p0)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_calrissian_lando)
                .nameAvatar(context.getString(R.string.avatar_calrissian_lando)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_chewbacca)
                .nameAvatar(context.getString(R.string.avatar_chewbacca)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_clone_trooper)
                .nameAvatar(context.getString(R.string.avatar_clone_trooper)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_darth_maul)
                .nameAvatar(context.getString(R.string.avatar_darth_maul)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_darth_vader)
                .nameAvatar(context.getString(R.string.avatar_darth_vader)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_emperor_palpatine)
                .nameAvatar(context.getString(R.string.avatar_emperor_palpatine)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_ewok)
                .nameAvatar(context.getString(R.string.avatar_ewok)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_five_red)
                .nameAvatar(context.getString(R.string.avatar_five_red)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_gon_jinn_qui)
                .nameAvatar(context.getString(R.string.avatar_gon_jinn_qui)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_greedo)
                .nameAvatar(context.getString(R.string.avatar_greedo)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_han_solo)
                .nameAvatar(context.getString(R.string.avatar_han_solo)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_hutt_jabba_the)
                .nameAvatar(context.getString(R.string.avatar_hutt_jabba_the)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_kenobi_obiwan)
                .nameAvatar(context.getString(R.string.avatar_kenobi_obiwan)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_luke_skywalker)
                .nameAvatar(context.getString(R.string.avatar_luke_skywalker)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_raider_tusken)
                .nameAvatar(context.getString(R.string.avatar_raider_tusken)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_stormtrooper)
                .nameAvatar(context.getString(R.string.avatar_stormtrooper)));
        avatars.add(new AvatarDto()
                .idResAvatar(R.drawable.avatar_yoda)
                .nameAvatar(context.getString(R.string.avatar_yoda)));
    }

    public List<AvatarDto> findAll() {
        Log.i(TAG, "Find all Avatars.");

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
