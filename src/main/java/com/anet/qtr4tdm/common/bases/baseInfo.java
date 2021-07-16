package com.anet.qtr4tdm.common.bases;

import java.security.acl.Owner;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

public class baseInfo {
    public BlockPos pos;
    public int OwnerId;
    public int id;
    public int level;
    public ChunkPos[] chunks;
    public int dimenision;

    public baseInfo (BlockPos pos, int OwnerId, int id, int level, ChunkPos[] chunks, int dimension) {
        this.pos = pos;
        this.OwnerId = OwnerId;
        this.id = id;
        this.level = level;
        this.chunks = chunks;
        this.dimenision = dimension;
    }

    public boolean ContainsChunk (ChunkPos pos) {
        for (ChunkPos chunk : chunks) {
            if (pos.equals(chunk)) return true;
        }
        return false;
    }
}
