package junjange.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import junjange.core.designsystem.theme.LottoTheme
import junjange.core.ui.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun LottoEditProfileBottomSheet(callback: (editProfileType: EditProfileType) -> Unit) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(300.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.TopCenter)
                    .clip(shape = RoundedCornerShape(100.dp))
                    .width(32.dp)
                    .height(4.dp)
                    .background(color = LottoTheme.colors.gray500),
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(44.dp))
            Text(
                modifier =
                    Modifier.then(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                    ),
                text = stringResource(Res.string.edit_profile_img),
                style = LottoTheme.typography.headline3,
                color = LottoTheme.colors.lottoBlack,
            )
            Spacer(modifier = Modifier.height(8.dp))
            LottoButtonBar(
                textRes = Res.string.profile_image_select,
                onClick = { callback(EditProfileType.ProfileImageSelect) },
            )

            Text(
                modifier =
                    Modifier.then(
                        Modifier
                            .fillMaxWidth()
                            .clickable { callback(EditProfileType.ProfileDefaultImageSelect) }
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                    ),
                text = stringResource(Res.string.profile_default_image_select),
                style = LottoTheme.typography.body1,
                color = LottoTheme.colors.lottoBlack,
            )
        }
    }
}

sealed class EditProfileType {
    data object ProfileImageSelect : EditProfileType()

    data object ProfileDefaultImageSelect : EditProfileType()
}
