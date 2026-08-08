# MoMomentum

> A fully configurable momentum progression plugin for Spigot/Paper servers.

MoMomentum allows server owners to create configurable momentum progression systems with levels, costs, messages, rewards, requirements and boosters.

Players can progress through configurable momentum levels by completing supported momentum events. Each level can define its own cost, boss bar message, completion actions and reduction actions.

MoMomentum integrates with MoBoosters to provide configurable boosts based on the player's current momentum progression.

---

# Features

* Configurable momentum levels
* Custom level costs
* Custom boss bar messages
* Level completion actions
* Level reduction actions
* Configurable requirements and rewards
* PlaceholderAPI support
* Chance-based rewards
* MoBoosters integration
* Public API and Events
* Fully configurable

---

# Installation

## Requirements

| Plugin         | Required |
| -------------- | -------- |
| MoCore         | ✅        |
| MoBoosters     | ✅        |
| PlaceholderAPI | Optional |

---

# API

MoMomentum includes a public API for integration with other plugins.

**Main Class:** `MomentumAPI`

---

# Events

| Event                         | Description                                       |
| ----------------------------- | ------------------------------------------------- |
| `MomentumCompletedLevelEvent` | Called when a player completes a momentum level.  |
| `MomentumReducedLevelEvent`   | Called when a player's momentum level is reduced. |

---

# Commands

| Command                | Description                               |
| ---------------------- | ----------------------------------------- |
| `/momomentum boosters` | Displays the available momentum boosters. |
| `/momomentum reload`   | Reloads the MoMomentum configuration.     |

---

# PlaceholderAPI

MoMomentum provides placeholders for accessing configured and active momentum information.

## Config Placeholders

| Placeholder                            | Description                                                      |
| -------------------------------------- | ---------------------------------------------------------------- |
| `%momomentum_cost_<level>%`            | Returns the configured cost for the specified level.             |
| `%momomentum_bossbar_message_<level>%` | Returns the configured boss bar message for the specified level. |
| `%momomentum_boost_<level>%`           | Returns the configured boost for the specified level.            |

## Actual Momentum

| Placeholder                           | Description                                                   |
| ------------------------------------- | ------------------------------------------------------------- |
| `%momomentum_actual_level%`           | Returns the player's current momentum level.                  |
| `%momomentum_actual_cost%`            | Returns the cost of the current level.                        |
| `%momomentum_actual_bossbar_message%` | Returns the current boss bar message.                         |
| `%momomentum_actual_time_formatted%`  | Returns the current momentum time in formatted form.          |
| `%momomentum_actual_time%`            | Returns the current momentum time.                            |
| `%momomentum_highest_time_formatted%` | Returns the player's highest momentum time in formatted form. |
| `%momomentum_highest_time%`           | Returns the player's highest momentum time.                   |
| `%momomentum_total_boost_<type>%`     | Returns the total boost for the specified type.               |

---

# Momentum Events

Momentum can react to configured Minecraft events.

| Event        | Description                                          |
| ------------ | ---------------------------------------------------- |
| `BlockBreak` | Handles momentum progression when blocks are broken. |

---

# Variables

MoMomentum provides variables that can be used in requirements, rewards and messages.

## Player

```text
%player%
```

## MomentumPlayer

| Variable           | Description                |
| ------------------ | -------------------------- |
| `%amount%`         | Current momentum amount.   |
| `%actual_level%`   | Current momentum level.    |
| `%actual_cost%`    | Current level cost.        |
| `%actual_message%` | Current level message.     |
| `%next_level%`     | Next momentum level.       |
| `%next_cost%`      | Cost of the next level.    |
| `%next_message%`   | Message of the next level. |

---

# Chance

Rewards can be executed using configurable chances.

```text
[CHANCE -> <percentage>]
```

Example:

```text
[CHANCE -> 50] MESSAGE -> &aYou received a reward!
```

---

# Requirements

Requirements determine whether an action can be executed.

Requirements support:

* PlaceholderAPI
* Player Variables
* MomentumPlayer Variables

## Requirement Types

| Type   | Description                                                      |
| ------ | ---------------------------------------------------------------- |
| `EVAL` | Evaluates placeholders and variables using configured operators. |

## String Operators

```text
equals
!equals
equalsIgnoreCase
!equalsIgnoreCase
startsWith
!startsWith
contains
!contains
```

## Number Operators

```text
<=
<
>=
>
==
!=
```

Multiple requirements can be separated using:

```text
||
```

Example:

```yaml
requirements:
  - '[EVAL] %actual_level% >= 5 || %player% equals ExamplePlayer'
```

---

# Rewards

Rewards are executed when the configured requirements are successfully completed.

Rewards can be chained using `&&` or provide alternative outcomes using `||`.

| Reward                 | Format                                      |
| ---------------------- | ------------------------------------------- |
| `CONSOLE_COMMAND`      | `CONSOLE_COMMAND -> <command>`              |
| `PLAYER_COMMAND`       | `PLAYER_COMMAND -> <command>`               |
| `PLAYER_COMMAND_AS_OP` | `PLAYER_COMMAND_AS_OP -> <command>`         |
| `TITLE`                | `TITLE -> <title>::<subtitle>`              |
| `SOUND`                | `SOUND -> <sound>::<volume>::<pitch>`       |
| `BROADCAST_MESSAGE`    | `BROADCAST_MESSAGE -> <message>`            |
| `BROADCAST_TITLE`      | `BROADCAST_TITLE -> <title>::<subtitle>`    |
| `JSON`                 | `JSON -> <json>`                            |
| `JSON_BROADCAST`       | `JSON_BROADCAST -> <json>`                  |
| `EFFECT`               | `EFFECT -> <effect>:<duration>:<amplifier>` |
| `EXECUTE_ACTION`       | `EXECUTE_ACTION -> <action>`                |

### Example

```text
[CHANCE -> 100] MESSAGE -> &aLevel completed!

[CHANCE -> 50] EFFECT -> SPEED::10::1

[CHANCE -> 100] CONSOLE_COMMAND -> give %player% diamond 1

[CHANCE -> 100] MESSAGE -> &aLevel completed! && SOUND -> LEVEL_UP::1::1
```

`EXECUTE_ACTION` can be used to execute another configured action from the same level.

---

# Levels

Momentum progression is configured through individual levels.

Each level can define:

* Level number
* Cost
* Boss bar message
* Completion actions
* Reduction actions

## Basic Structure

```yaml
Level-1:

  level: 1

  cost: 100

  boss-bar-text: '&eMomentum Level 1'

  actions:

    completed:
      ...

    reduced:
      ...
```

---

# Level Options

| Option              | Description                                           |
| ------------------- | ----------------------------------------------------- |
| `level`             | Defines the momentum level.                           |
| `cost`              | Defines the cost required for the level.              |
| `boss-bar-text`     | Defines the boss bar message displayed for the level. |
| `actions.completed` | Actions executed when the level is completed.         |
| `actions.reduced`   | Actions executed when the level is reduced.           |

---

# Level Actions

Each level can define actions for two different situations:

| Action      | Description                                   |
| ----------- | --------------------------------------------- |
| `completed` | Executed when the player completes the level. |
| `reduced`   | Executed when the player's level is reduced.  |

Both actions support:

* `cancel_action`
* `requirements`
* `rewards`
* `else`

---

# Completed Action

The `completed` action is executed when a player completes the configured momentum level.

```yaml
actions:

  completed:

    cancel_action: false

    requirements:
      - '[EVAL] %actual_level% >= 5'

    rewards:
      - '[CHANCE -> 100] MESSAGE -> &aMomentum level completed!'
```

---

# Reduced Action

The `reduced` action is executed when a player's momentum level is reduced.

```yaml
actions:

  reduced:

    cancel_action: false

    requirements:
      - '[EVAL] %actual_level% >= 5'

    rewards:
      - '[CHANCE -> 100] MESSAGE -> &cYour momentum level was reduced!'
```

---

# Action Options

| Option          | Description                                                |
| --------------- | ---------------------------------------------------------- |
| `cancel_action` | Determines whether the current action should be cancelled. |
| `requirements`  | Conditions that must be met before rewards are executed.   |
| `rewards`       | Rewards executed when requirements succeed.                |
| `else`          | Rewards executed when requirements fail.                   |

---

# Else Rewards

The `else` section is executed when the configured requirements are not met.

```yaml
actions:

  completed:

    requirements:
      - '[EVAL] %actual_level% >= 10'

    rewards:
      - '[CHANCE -> 100] MESSAGE -> &aRequirement met!'

    else:
      - '[CHANCE -> 100] MESSAGE -> &cRequirement not met.'
```

---

# Boosters

MoMomentum integrates with **MoBoosters** to provide configurable boosts based on momentum progression.

> **Note:** MoBoosters must be installed for momentum boosters to work.

A booster can be configured inside a momentum level using:

```yaml
boosters:

  Example:

    type: Personal
    applicator: MoMomentum
    boosted: example
    boost: 2.0
```

## Booster Options

| Option       | Description                                |
| ------------ | ------------------------------------------ |
| `type`       | Defines how the booster is applied.        |
| `applicator` | Defines the booster applicator.            |
| `boosted`    | Defines the value affected by the booster. |
| `boost`      | Defines the amount of the boost.           |

---

# Example Configuration

The following example demonstrates a basic momentum level with completion and reduction actions.

```yaml
Level-1:

  level: 1

  cost: 100

  boss-bar-text: '&eMomentum &8| &fLevel &a1'

  actions:

    completed:

      cancel_action: false

      requirements:
        - '[EVAL] %actual_level% >= 1'

      rewards:
        - '[CHANCE -> 100] MESSAGE -> &e&lMOMENTUM! &aYou completed level %actual_level%!'
        - '[CHANCE -> 100] SOUND -> LEVEL_UP::1::1'

      else:
        - '[CHANCE -> 100] MESSAGE -> &cYou do not meet the requirements.'

    reduced:

      cancel_action: false

      requirements:
        - '[EVAL] %actual_level% >= 1'

      rewards:
        - '[CHANCE -> 100] MESSAGE -> &cYour momentum level was reduced!'

      else:
        - '[CHANCE -> 100] MESSAGE -> &cYou do not meet the requirements.'

  boosters:

    Example:

      type: Personal
      applicator: MoMomentum
      boosted: example
      boost: 2.0
```

---
