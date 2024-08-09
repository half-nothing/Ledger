package com.github.quiltservertools.ledger.commands.subcommands

import com.github.quiltservertools.ledger.Ledger
import com.github.quiltservertools.ledger.commands.BuildableCommand
import com.github.quiltservertools.ledger.commands.CommandConsts
import com.github.quiltservertools.ledger.database.ActionQueueService
import com.github.quiltservertools.ledger.utility.Context
import com.github.quiltservertools.ledger.utility.LiteralNode
import com.github.quiltservertools.ledger.utility.TextColorPallet
import kotlinx.coroutines.launch
import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.server.command.CommandManager
import net.minecraft.text.Text

object FlushCommand: BuildableCommand {
    override fun build(): LiteralNode =
        CommandManager.literal("flush")
            .requires(Permissions.require("ledger.commands.flush", CommandConsts.PERMISSION_LEVEL))
            .executes { flush(it) }
            .build()

    private fun flush(context: Context): Int {
        Ledger.launch {
            val source = context.source
            ActionQueueService.flushQueue()
            source.sendFeedback(
                {
                    Text.translatable("text.ledger.flush.message")
                        .setStyle(TextColorPallet.primary)
                },
                false
            )
        }

        return 1
    }
}