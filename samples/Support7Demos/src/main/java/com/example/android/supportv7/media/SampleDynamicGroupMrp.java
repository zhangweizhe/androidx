/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.supportv7.media;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.mediarouter.media.MediaRouteDescriptor;
import androidx.mediarouter.media.MediaRouteProvider;
import androidx.mediarouter.media.MediaRouteProviderDescriptor;
import androidx.mediarouter.media.MediaRouter.ControlRequestCallback;

import com.example.android.supportv7.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Demonstrates how to create a custom media route provider.
 *
 * @see SampleDynamicGroupMrpService
 */
final class SampleDynamicGroupMrp extends SampleMediaRouteProvider {
    private static final String TAG = "SampleDynamicGroupMrp";

    private static final String FIXED_VOLUME_ROUTE_ID = "fixed";
    private static final String VARIABLE_VOLUME_BASIC_ROUTE_ID = "variable_basic";

    SampleDynamicGroupMrp(Context context) {
        super(context);
    }

    @Override
    public RouteController onCreateRouteController(String routeId) {
        return new SampleDynamicGroupRouteController(routeId);
    }

    @Override
    public DynamicGroupRouteController onCreateDynamicGroupRouteController(
            String initialMemberRouteId) {
        String dynamicGroupRouteId = "DG_" + initialMemberRouteId;

        // Create a new media route descriptor for dynamic group route, if it does not exist.
        if (!mRouteDescriptors.containsKey(dynamicGroupRouteId)) {
            MediaRouteDescriptor initMemberDescriptor = mRouteDescriptors.get(initialMemberRouteId);
            if (initMemberDescriptor == null || !initMemberDescriptor.isValid()) {
                Log.w(TAG, "initial route doesn't exist or isn't valid : " + initialMemberRouteId);
                return null;
            }
            MediaRouteDescriptor descriptor = new MediaRouteDescriptor.Builder(initMemberDescriptor)
                    .setId(dynamicGroupRouteId)
                    .setIsDynamicGroupRoute(true)
                    .setName(initMemberDescriptor.getName() + " (Group)")
                    .build();
            mRouteDescriptors.put(dynamicGroupRouteId, descriptor);
        }

        DynamicGroupRouteController controller =
                new SampleDynamicGroupRouteController(dynamicGroupRouteId);
        // Add initial member route.
        controller.onAddMemberRoute(initialMemberRouteId);
        return controller;
    }

    @Override
    protected void initializeRoutes() {
        Resources r = getContext().getResources();
        Intent settingsIntent = new Intent(Intent.ACTION_MAIN);
        settingsIntent.setClass(getContext(), SampleMediaRouteSettingsActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        IntentSender is = PendingIntent.getActivity(getContext(), 99, settingsIntent,
                PendingIntent.FLAG_UPDATE_CURRENT).getIntentSender();

        MediaRouteDescriptor routeDescriptor1 = new MediaRouteDescriptor.Builder(
                VARIABLE_VOLUME_BASIC_ROUTE_ID + "1",
                r.getString(R.string.dg_tv_route_name1))
                .setDescription(r.getString(R.string.sample_route_description))
                .addControlFilters(CONTROL_FILTERS_BASIC)
                .setDeviceType(MediaRouter.RouteInfo.DEVICE_TYPE_TV)
                .setPlaybackStream(AudioManager.STREAM_MUSIC)
                .setPlaybackType(MediaRouter.RouteInfo.PLAYBACK_TYPE_REMOTE)
                .setVolumeHandling(MediaRouter.RouteInfo.PLAYBACK_VOLUME_VARIABLE)
                .setVolumeMax(VOLUME_MAX)
                .setVolume(mVolume)
                .setCanDisconnect(true)
                .setSettingsActivity(is)
                .build();

        MediaRouteDescriptor routeDescriptor2 = new MediaRouteDescriptor.Builder(
                VARIABLE_VOLUME_BASIC_ROUTE_ID + "2",
                r.getString(R.string.dg_tv_route_name2))
                .setDescription(r.getString(R.string.sample_route_description))
                .addControlFilters(CONTROL_FILTERS_BASIC)
                .setDeviceType(MediaRouter.RouteInfo.DEVICE_TYPE_TV)
                .setPlaybackStream(AudioManager.STREAM_MUSIC)
                .setPlaybackType(MediaRouter.RouteInfo.PLAYBACK_TYPE_REMOTE)
                .setVolumeHandling(MediaRouter.RouteInfo.PLAYBACK_VOLUME_VARIABLE)
                .setVolumeMax(VOLUME_MAX)
                .setVolume(mVolume)
                .setCanDisconnect(true)
                .setSettingsActivity(is)
                .build();

        MediaRouteDescriptor routeDescriptor3 = new MediaRouteDescriptor.Builder(
                VARIABLE_VOLUME_BASIC_ROUTE_ID + "3",
                r.getString(R.string.dg_speaker_route_name3))
                .setDescription(r.getString(R.string.sample_route_description))
                .addControlFilters(CONTROL_FILTERS_BASIC)
                .setDeviceType(MediaRouter.RouteInfo.DEVICE_TYPE_SPEAKER)
                .setPlaybackStream(AudioManager.STREAM_MUSIC)
                .setPlaybackType(MediaRouter.RouteInfo.PLAYBACK_TYPE_REMOTE)
                .setVolumeHandling(MediaRouter.RouteInfo.PLAYBACK_VOLUME_VARIABLE)
                .setVolumeMax(VOLUME_MAX)
                .setVolume(mVolume)
                .setCanDisconnect(true)
                .setSettingsActivity(is)
                .build();

        MediaRouteDescriptor routeDescriptor4 = new MediaRouteDescriptor.Builder(
                VARIABLE_VOLUME_BASIC_ROUTE_ID + "4",
                r.getString(R.string.dg_speaker_route_name4))
                .setDescription(r.getString(R.string.sample_route_description))
                .addControlFilters(CONTROL_FILTERS_BASIC)
                .setDeviceType(MediaRouter.RouteInfo.DEVICE_TYPE_SPEAKER)
                .setPlaybackStream(AudioManager.STREAM_MUSIC)
                .setPlaybackType(MediaRouter.RouteInfo.PLAYBACK_TYPE_REMOTE)
                .setVolumeHandling(MediaRouter.RouteInfo.PLAYBACK_VOLUME_VARIABLE)
                .setVolumeMax(VOLUME_MAX)
                .setVolume(mVolume)
                .setCanDisconnect(true)
                .setSettingsActivity(is)
                .build();

        MediaRouteDescriptor routeDescriptor5 = new MediaRouteDescriptor.Builder(
                FIXED_VOLUME_ROUTE_ID,
                r.getString(R.string.dg_not_unselectable_route_name5))
                .setDescription(r.getString(R.string.sample_route_description))
                .addControlFilters(CONTROL_FILTERS_BASIC)
                .setPlaybackStream(AudioManager.STREAM_MUSIC)
                .setPlaybackType(MediaRouter.RouteInfo.PLAYBACK_TYPE_REMOTE)
                .setVolumeHandling(MediaRouter.RouteInfo.PLAYBACK_VOLUME_FIXED)
                .setVolumeMax(VOLUME_MAX)
                .setVolume(VOLUME_MAX)
                .setCanDisconnect(true)
                .setSettingsActivity(is)
                .build();

        mRouteDescriptors.put(routeDescriptor1.getId(), routeDescriptor1);
        mRouteDescriptors.put(routeDescriptor2.getId(), routeDescriptor2);
        mRouteDescriptors.put(routeDescriptor3.getId(), routeDescriptor3);
        mRouteDescriptors.put(routeDescriptor4.getId(), routeDescriptor4);
        mRouteDescriptors.put(routeDescriptor5.getId(), routeDescriptor5);
    }

    @Override
    protected void publishRoutes() {
        MediaRouteProviderDescriptor providerDescriptor = new MediaRouteProviderDescriptor.Builder()
                .setSupportsDynamicGroupRoute(true)
                .addRoutes(mRouteDescriptors.values())
                .build();
        setDescriptor(providerDescriptor);
    }

    final class SampleDynamicGroupRouteController
            extends MediaRouteProvider.DynamicGroupRouteController {
        private final String mRouteId;
        private final RouteControlHelper mHelper;

        private List<String> mMemberRouteIds = new ArrayList<>();
        private Map<String, DynamicRouteDescriptor> mDynamicRouteDescriptors = new HashMap<>();
        private DynamicGroupRouteController.OnDynamicRoutesChangedListener
                mDynamicRoutesChangedListener;
        private Executor mListenerExecutor;

        SampleDynamicGroupRouteController(String dynamicGroupRouteId) {
            mRouteId = dynamicGroupRouteId;

            // Initialize DynamicRouteDescriptor with all the route descriptors.
            List<MediaRouteDescriptor> routeDescriptors = getDescriptor().getRoutes();
            if (routeDescriptors != null && !routeDescriptors.isEmpty()) {
                for (MediaRouteDescriptor descriptor: routeDescriptors) {
                    String routeId = descriptor.getId();
                    DynamicRouteDescriptor.Builder builder =
                            new DynamicRouteDescriptor.Builder(descriptor)
                                    .setIsGroupable(true)
                                    .setIsTransferable(true)
                                    .setSelectionState(DynamicRouteDescriptor.UNSELECTED);
                    mDynamicRouteDescriptors.put(routeId, builder.build());
                }
            }

            mHelper = new RouteControlHelper(mRouteId);
            Log.d(TAG, mRouteId + ": Controller created.");
        }

        //////////////////////////////////////////////
        // Overrides DynamicGroupRouteController
        //////////////////////////////////////////////

        @Override
        public String getDynamicGroupRouteId() {
            return mRouteId;
        }

        @Override
        public String getGroupableSelectionTitle() {
            return "Add a device";
        }

        @Override
        public String getTransferableSectionTitle() {
            return "Play on tv";
        }

        @Override
        public void onUpdateMemberRoutes(List<String> routeIds) {
            mMemberRouteIds = routeIds;
        }

        @Override
        public void onAddMemberRoute(String routeId) {
            DynamicRouteDescriptor dynamicDescriptor = mDynamicRouteDescriptors.get(routeId);
            if (dynamicDescriptor == null) {
                Log.d(TAG, "onAddMemberRoute: Ignored for routeId: " + routeId);
                return;
            }

            MediaRouteDescriptor routeDescriptor = dynamicDescriptor.getRouteDescriptor();
            DynamicRouteDescriptor.Builder builder =
                    new DynamicRouteDescriptor.Builder(dynamicDescriptor)
                            .setSelectionState(DynamicRouteDescriptor.SELECTED);

            // Make fixed volume route not unselectable.
            if (routeDescriptor.getVolumeHandling()
                    == MediaRouter.RouteInfo.PLAYBACK_VOLUME_FIXED) {
                builder.setIsUnselectable(false);
            } else {
                builder.setIsUnselectable(true);
            }
            mDynamicRouteDescriptors.put(routeId, builder.build());
            mMemberRouteIds.add(routeId);

            MediaRouteDescriptor groupDescriptor =
                    new MediaRouteDescriptor.Builder(mRouteDescriptors.get(mRouteId))
                            .addGroupMemberId(routeId)
                            .build();
            mRouteDescriptors.put(mRouteId, groupDescriptor);

            if (routeDescriptor.getDeviceType() == MediaRouter.RouteInfo.DEVICE_TYPE_TV) {
                // Make other TVs ungroupable, since only one TV can exist in dynamic group.
                setOtherTvDevicesGroupable(false);
            }
            publishRoutes();
            if (mListenerExecutor != null) {
                mListenerExecutor.execute(() -> mDynamicRoutesChangedListener.onRoutesChanged(
                        SampleDynamicGroupRouteController.this,
                        mDynamicRouteDescriptors.values()));
            }
        }

        @Override
        public void onRemoveMemberRoute(String routeId) {
            DynamicRouteDescriptor dynamicDescriptor = mDynamicRouteDescriptors.get(routeId);
            if (dynamicDescriptor == null || !dynamicDescriptor.isUnselectable()
                    || !mMemberRouteIds.remove(routeId)) {
                Log.d(TAG, "onRemoveMemberRoute: Ignored for routeId: " + routeId);
                return;
            }

            MediaRouteDescriptor routeDescriptor = dynamicDescriptor.getRouteDescriptor();
            DynamicRouteDescriptor.Builder builder =
                    new DynamicRouteDescriptor.Builder(dynamicDescriptor);
            builder.setSelectionState(DynamicRouteDescriptor.UNSELECTED);
            mDynamicRouteDescriptors.put(routeId, builder.build());

            MediaRouteDescriptor groupDescriptor =
                    new MediaRouteDescriptor.Builder(mRouteDescriptors.get(mRouteId))
                            .removeGroupMemberId(routeId)
                            .build();
            mRouteDescriptors.put(mRouteId, groupDescriptor);

            if (routeDescriptor.getDeviceType() == MediaRouter.RouteInfo.DEVICE_TYPE_TV) {
                // Revert other TV devices to groupable routes.
                setOtherTvDevicesGroupable(true);
            }
            publishRoutes();
            if (mListenerExecutor != null) {
                mListenerExecutor.execute(() -> mDynamicRoutesChangedListener.onRoutesChanged(
                        SampleDynamicGroupRouteController.this,
                        mDynamicRouteDescriptors.values()));
            }
        }

        @Override
        public void setOnDynamicRoutesChangedListener(
                @NonNull Executor executor, OnDynamicRoutesChangedListener listener) {
            if (executor == null) {
                throw new IllegalArgumentException("Executor shouldn't be null.");
            }
            mDynamicRoutesChangedListener = listener;
            mListenerExecutor = executor;
            if (mDynamicRoutesChangedListener == null) {
                return;
            }
            mListenerExecutor.execute(() -> mDynamicRoutesChangedListener.onRoutesChanged(
                    SampleDynamicGroupRouteController.this,
                    mDynamicRouteDescriptors.values()));

        }

        //////////////////////////////////////////////
        // Overrides RouteController
        //////////////////////////////////////////////

        @Override
        public void onRelease() {
            Log.d(TAG, mRouteId + ": Controller released");
            mHelper.onRelease();
        }

        @Override
        public void onSelect() {
            Log.d(TAG, mRouteId + ": Selected");
            mHelper.onSelect();
        }

        @Override
        public void onUnselect() {
            Log.d(TAG, mRouteId + ": Unselected");
            mHelper.onUnselect();

            if (mRouteDescriptors.get(mRouteId).isDynamicGroupRoute()) {
                mRouteDescriptors.remove(mRouteId);
            }
            publishRoutes();
        }

        @Override
        public void onSetVolume(int volume) {
            Log.d(TAG, mRouteId + ": Set volume to " + volume);
            mHelper.onSetVolume(volume);
        }

        @Override
        public void onUpdateVolume(int delta) {
            Log.d(TAG, mRouteId + ": Update volume by " + delta);
            mHelper.onUpdateVolume(delta);
        }

        @Override
        public boolean onControlRequest(Intent intent, ControlRequestCallback callback) {
            Log.d(TAG, mRouteId + ": Received control request " + intent);
            return mHelper.onControlRequest(intent, callback);
        }

        // Set groupable attribute of other TV devices who is not member of dynamic group.
        private void setOtherTvDevicesGroupable(boolean groupable) {
            for (DynamicRouteDescriptor dynamicDescriptor : mDynamicRouteDescriptors.values()) {
                MediaRouteDescriptor routeDescriptor = dynamicDescriptor.getRouteDescriptor();
                String routeId = routeDescriptor.getId();
                if (routeDescriptor.getDeviceType() == MediaRouter.RouteInfo.DEVICE_TYPE_TV
                        && !mMemberRouteIds.contains(routeId)) {
                    DynamicRouteDescriptor.Builder builder =
                            new DynamicRouteDescriptor.Builder(dynamicDescriptor)
                                    .setIsGroupable(groupable);
                    mDynamicRouteDescriptors.put(routeId, builder.build());
                }
            }
        }
    }
}
