/*
 * Copyright 2026 m.bignami.
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
package net.unknowndomain.alea.systems.shadowrun5;

import net.unknowndomain.alea.systems.RpgSystemRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards this module's ServiceLoader/module-info registration of {@link Shadowrun5Command}
 * as a {@code RpgSystemCommand} provider, and the JLS 12.4.2 circular class-init
 * bug that {@link RpgSystemRegistry} was split out to avoid (a broken registration
 * would surface here as an empty list or as unset static fields on the provider).
 */
class Shadowrun5CommandRegistrationTest
{
    @Test
    void exactlyOneProviderIsRegistered()
    {
        assertEquals(1, RpgSystemRegistry.SYSTEMS.size());
        assertTrue(RpgSystemRegistry.SYSTEMS.get(0) instanceof Shadowrun5Command);
    }

    @Test
    void descriptorIsFullyInitialized()
    {
        var desc = RpgSystemRegistry.SYSTEMS.get(0).getCommandDesc();
        assertNotNull(desc);
        assertFalse(desc.getSystem().isBlank());
        assertFalse(desc.getShortcut().isBlank());
        assertFalse(desc.getCommand().isBlank());
    }

    @Test
    void checkCommandMatchesItsOwnShortcutAndCommand()
    {
        var command = RpgSystemRegistry.SYSTEMS.get(0);
        var desc = command.getCommandDesc();
        assertTrue(command.checkCommand(desc.getShortcut()));
        assertTrue(command.checkCommand(desc.getCommand()));
        assertFalse(command.checkCommand("zzz-not-a-real-command-zzz"));
    }

    @Test
    void buildOptionsReturnsANonNullInstance()
    {
        assertNotNull(RpgSystemRegistry.SYSTEMS.get(0).buildOptions());
    }
}
