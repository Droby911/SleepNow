# SleepNow
Плагин для Minecraft 1.16.4 (Spigot)
- Позволяет пропустить ночь, если на сервере одновременно спит определенное количество человек. 
- Отображает в чате, сколько игроков нужно, чтобы пропустить ночь.

Что нужно реализовать:
1. Проверить работу плагина, когда на сервере играет больше одного человека.
2. Сделать перевод на английский
3. Улучшить видимость текста в чате

ЭТО СУПЕР-ТЕСТОВАЯ ВЕРСИЯ.
ЧТОБЫ ЭТОТ ПЛАГИН МОГ НОРМАЛЬНО РАБОТАТЬ, МНЕ НАДО ЗНАТЬ, КАК ОН РАБОТАЕТ КОГДА НА СЕРВЕРЕ БОЛЬШЕ ОДНОГО ЧЕЛОВЕКА (а лучше больше трех или четырех).
Для чего: проблема в том, что по плану должно быть так: чем больше игроков -> тем на большее число плагин делит количество игроков, нужное для скипа. Т.е если на сервере в общем 8 человек, то плагин делит на 3, итого 2 человека нужно, чтобы скипнуть ночь. Если 16, то будет делить на 4, к примеру. Но полную механику я пока не прописывал, и не пропишу на данный момент, так как нужны постоянные тесты с некоторым количеством людей, а с этим проблема. Плагин пока работает так: если на сервере 4 человека или больше и в то же время меньше 8 человек, то плагин делит общее число игроков на 3, и полученный результат будет означать, сколько людей должно лежать в кровати, чтобы скипнуть ночь. Если под эти условия не попадает, то тогда даже если один игрок спит, то ночь скипается. Конечно, такого не должно быть если на сервере больше восьми человек, но это опять же, тестовая версия.
