import { useNavigate } from 'react-router-dom'
import api from '../api/axios'
import VoteButtons from './VoteButtons'
import { useAuth } from '../context/AuthContext'

export default function PostCard({ post, onChange }) {
  const navigate = useNavigate()
  const { user } = useAuth()

  async function handleVote(voteType) {
    if (!user) return navigate('/login')
    await api.post(`/posts/${post.id}/vote`, { voteType })
    onChange && onChange()
  }

  return (
    <div className="card post-row">
      <VoteButtons score={post.score} currentVote={post.currentUserVote} onVote={handleVote} />
      <div style={{ flex: 1 }}>
        <div className="post-meta">
          r/{post.communityName} · posted by u/{post.authorUsername}
        </div>
        <div className="post-title">
          <a onClick={() => navigate(`/post/${post.id}`)} style={{ cursor: 'pointer' }}>
            {post.title}
          </a>
        </div>
        {post.content && <div className="post-content">{post.content.slice(0, 200)}{post.content.length > 200 ? '…' : ''}</div>}
        <div className="muted" style={{ marginTop: 6 }}>{post.commentCount} comments</div>
      </div>
    </div>
  )
}
