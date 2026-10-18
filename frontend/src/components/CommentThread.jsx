import { useState } from 'react'
import api from '../api/axios'
import { useAuth } from '../context/AuthContext'
import { useNavigate } from 'react-router-dom'

export default function CommentThread({ comment, postId, onChange, depth = 0 }) {
  const [replying, setReplying] = useState(false)
  const [text, setText] = useState('')
  const { user } = useAuth()
  const navigate = useNavigate()

  async function vote(voteType) {
    if (!user) return navigate('/login')
    await api.post(`/comments/${comment.id}/vote`, { voteType })
    onChange && onChange()
  }

  async function submitReply(e) {
    e.preventDefault()
    if (!user) return navigate('/login')
    await api.post('/comments', { postId, content: text, parentId: comment.id })
    setText('')
    setReplying(false)
    onChange && onChange()
  }

  return (
    <div className="comment" style={{ marginLeft: depth > 0 ? 12 : 0 }}>
      <div className="post-meta">u/{comment.authorUsername}</div>
      <div className="comment-body">{comment.content}</div>
      <div className="comment-actions">
        <button className="vote-btn" onClick={() => vote('UP')}>▲</button>
        <span>{comment.score}</span>
        <button className="vote-btn" onClick={() => vote('DOWN')}>▼</button>
        <button className="vote-btn" onClick={() => setReplying(!replying)}>reply</button>
      </div>

      {replying && (
        <form className="stacked" onSubmit={submitReply} style={{ marginTop: 6 }}>
          <textarea value={text} onChange={(e) => setText(e.target.value)} placeholder="Write a reply..." />
          <button className="btn" style={{ alignSelf: 'flex-start', marginTop: 6 }} type="submit">Reply</button>
        </form>
      )}

      {comment.replies && comment.replies.map((r) => (
        <CommentThread key={r.id} comment={r} postId={postId} onChange={onChange} depth={depth + 1} />
      ))}
    </div>
  )
}
