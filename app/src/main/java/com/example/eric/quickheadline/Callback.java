/*
 * Copyright (C) 2018 Eric Afenyo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.eric.quickheadline;

import android.support.annotation.Nullable;

/**
 * Created by eric on 06/03/2018.
 */

public interface Callback {

    /**
     * can be called when a particular operation finished successfully
     *
     * @param data any {@link Object} type
     */
    void onSuccess(@Nullable Object data);

    /**
     * can be called when a particular operation failed to execute
     *
     * @param error any {@link Object} type
     */
    void onFailure(@Nullable Object error);

    /**
     * can be called to signal an empty state
     */
    void onEmpty();

    /**
     * can be called to signal a null state
     */
    void onNull();
}
