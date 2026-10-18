import { useState } from 'react'

export default function VoteButtons({ score, currentVote, onVote }) {
  const [busy, setBusy] = useState(false)

  async function cast(type) {
    if (busy) return
    setBusy(true)
    try {
      await onVote(type)
    } finally {
      setBusy(false)
    }
  }

  return (
    <div className="vote-col">
      <button
        className={`vote-btn ${currentVote === 'UP' ? 'active-up' : ''}`}
        onClick={() => cast('UP')}
        disabled={busy}
      >▲</button>
      <span>{score}</span>
      <button
        className={`vote-btn ${currentVote === 'DOWN' ? 'active-down' : ''}`}
        onClick={() => cast('DOWN')}
        disabled={busy}
      >▼</button>
    </div>
  )
}
